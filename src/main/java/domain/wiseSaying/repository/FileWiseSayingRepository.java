package domain.wiseSaying.repository;

import domain.wiseSaying.entity.WiseSaying;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 역할 : 데이터의 조회/수정/삭제/생성을 담당
 * 스캐너, 출력 금지
 */
public class FileWiseSayingRepository implements WiseSayingRepository{
    private Map<Integer, WiseSaying> map = new TreeMap<>(Collections.reverseOrder());
    private int id;

    public FileWiseSayingRepository() {
        //초기작업
        id = getLastId();
        loadAll();
    }

    @Override
    public int save(String author, String content) {
        //객체 생성
        WiseSaying saying = new WiseSaying(++id, author, content);

        //map에 추가
        map.put(saying.getId(), saying);

        //파일로 저장
        saveJsonFile(saying);

        //Id 업데이트
        saveLastId(id);

        return id;
    }

    @Override
    public boolean delete(int id) {

        if(findById(id) == null) return false;
        else {
            map.remove(id);
            deleteJsonFile(id);
            return true;
        }
    }

    @Override
    public void update(int targetId, String author, String content) {
        WiseSaying saying = findById(targetId);

        //객체 수정
        saying.setContent(content);
        saying.setAuthor(author);

        map.put(targetId, saying);
        saveJsonFile(saying);
    }

    @Override
    public List<WiseSaying> getList(String keywordType, String keyword) {

        //전체 목록 조회
        if(keyword == null || keyword.isEmpty()) {
            return new ArrayList<>(map.values());
        }

        //검색 결과 반환
        return map.values().stream()
                .filter(ws -> {
                    if (keywordType == null || keywordType.isEmpty()) {
                        return ws.getContent().contains(keyword) || ws.getAuthor().contains(keyword);
                    }
                    else if("content".equalsIgnoreCase(keywordType)) {
                        return ws.getContent().contains(keyword);
                    }
                    else if("author".equalsIgnoreCase(keywordType)) {
                        return ws.getAuthor().contains(keyword);
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    @Override
    public WiseSaying findById(int id) {
        return map.get(id);
    }

    public int getLastId() {
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

    public void loadAll() {
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

    @Override
    public boolean buildJsonFile() {
        String path = "db/wiseSaying/data.json";
        StringBuilder sb = new StringBuilder();

        if(map.isEmpty()) sb.append("[\n").append("]");
        else {
            sb.append("[\n");

            for(WiseSaying saying : map.values()) {
                sb.append(saying.toJson()).append(",").append("\n");
            }

            sb.setLength(sb.length()-2); //마지막에 뒤에 2글자 제거
            sb.append("\n").append("]");
        }

        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            bw.write(sb.toString());
            bw.close();
            return true;
        } catch(Exception e) {
            System.out.println("data.json 파일 빌드 실패");
            return false;
        }
    }


    void saveJsonFile(WiseSaying saying) {
        try {
            String path = "db/wiseSaying/" + saying.getId() + ".json";
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            bw.write(saying.toJson());
            bw.close();
        } catch(Exception e) {
            System.out.println("명언 파일 저장 실패");
        }
    }

    void deleteJsonFile(int id) {
        File targetFile = new File("db/wiseSaying/" + id + ".json");
        if(!targetFile.exists()) {
            System.out.printf("%d번 명언 파일 존재하지 않음\n", id);
        }
        boolean deleted = targetFile.delete();
        System.out.println(deleted);
    }

    public void saveLastId(int id) {
        try {
            String path = "db/wiseSaying/lastId.txt";
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            bw.write(String.valueOf(id));
            bw.close();

        } catch(Exception e) {
            System.out.println("id 파일 저장 실패");
        }
    }

    String extractValue(String json, String key) {
        int idx = json.indexOf(key);
        int start = json.indexOf(":", idx)+1; //: 다음부터 나오는 값 추출해야함
        int end = json.indexOf(",", start);
        if (end==-1) end = json.indexOf("}", start);

        String value = json.substring(start, end).trim();
        if(value.startsWith("\"")) value = value.substring(1, value.length()-1);

        return value;
    }
}
