import java.io.*;
import java.util.*;

public class Main {

    static Map<Integer, WiseSaying> map = new TreeMap<>(Collections.reverseOrder()); //아이디 기준 내림차순

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StringBuffer sb = new StringBuffer();

        int id = getLastId();
        loadWiseSayings();

        System.out.println("== 명언 앱 ==");
        while(true) {
            //명령 입력
            System.out.print("명령) ");
            String[] input = sc.nextLine().trim().split("\\?id=");
            String command = input[0];

            //명령 == 종료
            if(command.equals("종료")) break;

            //명령 == 등록
            else if(command.equals("등록")) {
                //명언 입력
                System.out.print("명언 : ");
                String content = sc.nextLine();

                //작가 입력
                System.out.print("작가 : ");
                String author = sc.nextLine();

                //map에 추가
                WiseSaying newWiseSaying = new WiseSaying(++id, author, content);
                map.put(id, newWiseSaying );

                //파일로 저장
                saveToJson(newWiseSaying);
                System.out.printf("%d번 명언이 등록되었습니다.\n", id);

                //id 업데이트
                saveLastId(id);
            }

            //명령 == 목록
            else if(command.equals("목록")) {
                //명언 목록(map) 조회
                for(int i : map.keySet()) {
                    sb.append(map.get(i).getSaying()).append("\n");
                }

                //출력
                System.out.print(sb);

                //초기화
                sb.setLength(0);
            }

            //명령 == 삭제
            else if(command.equals("삭제")) {
                try {
                    int targetId = Integer.parseInt(input[1]);
                    WiseSaying targetWiseSaying = map.get(targetId);

                    if(targetWiseSaying == null) System.out.printf("%d번 명언은 존재하지 않습니다.\n", targetId);
                    else {
                        map.remove(targetId);
                        deleteJsonFile(targetId);
                        System.out.printf("%d번 명언이 삭제되었습니다.\n", targetId);
                    }
                }
                catch(Exception e){
                    System.out.println("다시 입력해주세요.");
                }

            }

            //명령 수정
            else if(command.equals("수정")) {
                try {
                    int targetId = Integer.parseInt(input[1]);
                    WiseSaying targetWiseSaying = map.get(targetId);

                    if(targetWiseSaying == null) System.out.printf("%d번 명언은 존재하지 않습니다.\n", targetId);
                    else {
                        System.out.printf("명언(기존) : %s\n", targetWiseSaying.content);
                        System.out.print("명언 : ");
                        targetWiseSaying.content = sc.nextLine();

                        System.out.printf("작가(기존) : %s\n", targetWiseSaying.author);
                        System.out.print("작가 : ");
                        targetWiseSaying.author = sc.nextLine();

                        //파일도 수정
                        saveToJson(targetWiseSaying);
                    }
                }
                catch(Exception e){
                    System.out.println("다시 입력해주세요.");
                }

            }
            else if(command.equals("빌드")) {
                buildJsonFile();
            }

            else {
                System.out.println("다시 입력해주세요.");
            }
        }
    }

    static void saveToJson(WiseSaying saying) {
        try {
            String path = "db/wiseSaying/" + saying.id + ".json";
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            bw.write(saying.toJson());
            bw.close();
        } catch(Exception e) {
            System.out.println("명언 파일 저장 실패");
        }
    }

    static void deleteJsonFile(int id) {
        File targetFile = new File("db/wiseSaying/" + id + ".json");
        if(!targetFile.exists()) {
            System.out.printf("%d번 명언 파일 존재하지 않음\n", id);
        }
        boolean deleted = targetFile.delete();
        System.out.println(deleted);
    }

    static void saveLastId(int id) {
        try {
            String path = "db/wiseSaying/lastId.txt";
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            bw.write(String.valueOf(id));
            bw.close();

        } catch(Exception e) {
            System.out.println("id 파일 저장 실패");
        }
    }

    static int getLastId() {
        String path = "db/wiseSaying/lastId.txt";
        File lastIdFile = new File(path);
        int id = 0;

        try {
            if(!lastIdFile.exists()) {
                saveLastId(0);
                return id;
            }
            try(Scanner fs = new Scanner(lastIdFile)) {
                if(fs.hasNextInt()) id = fs.nextInt();
            }
        } catch (Exception e) {
            System.out.println("id 파일 읽기 실패");
        }
        return id;
    }

    static void loadWiseSayings() {
        String dir_path = "db/wiseSaying";
        File dir = new File(dir_path);

        File[] files = dir.listFiles((d, name) -> name.endsWith(".json") && !name.equals("data.json"));

        if(files == null) return;

        for(File file : files) {
            try{
                BufferedReader br = new BufferedReader(new FileReader(file));
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();

                String json = sb.toString();

                int id = Integer.parseInt(extractValue(json, "\"id\""));
                String content = extractValue(json, "\"content\"");
                String author = extractValue(json, "\"author\"");

                WiseSaying newWiseSaying = new WiseSaying(id, author, content);

                map.put(id, newWiseSaying);

            } catch(Exception e) {
                System.out.println("기존 명언 로드 실패: " +e.getMessage());
            }
        }
    }
    static String extractValue(String json, String key) {
        int idx = json.indexOf(key);
        int start = json.indexOf(":", idx)+1; //: 다음부터 나오는 값 추출해야함
        int end = json.indexOf(",", start);
        if (end==-1) end = json.indexOf("}", start);

        String value = json.substring(start, end).trim();
        if(value.startsWith("\"")) value = value.substring(1, value.length()-1);

        return value;
    }
    static void buildJsonFile() {
        String path = "db/wiseSaying/data.json";
        StringBuilder sb = new StringBuilder();

        sb.append("[\n");

        for(WiseSaying saying : map.values()) {
            sb.append(saying.toJson()).append(",").append("\n");
        }

        sb.setLength(sb.length()-2); //마지막에 뒤에 2글자 제거
        sb.append("\n").append("]");

        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            bw.write(sb.toString());
            bw.close();
            System.out.println("data.json 파일의 내용이 갱신되었습니다.");
        } catch(Exception e) {
            System.out.println("data.json 파일 빌드 실패");
        }
    }
}


