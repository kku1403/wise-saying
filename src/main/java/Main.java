import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StringBuffer sb = new StringBuffer();

        int id = 1;
        Map<Integer, WiseSaying> map = new TreeMap<>(Collections.reverseOrder()); //아이디 기준 내림차순

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
                map.put(id, new WiseSaying(id, author, content));
                System.out.printf("%d번 명언이 등록되었습니다.\n",id);

                //다음 번호
                id++;
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
                        System.out.printf("%d번 명언이 삭제되었습니다.\n", targetId);
                    }
                }
                catch(NumberFormatException e){
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
                    }
                }
                catch(NumberFormatException e){
                    System.out.println("다시 입력해주세요.");
                }

            }

            else {
                System.out.println("다시 입력해주세요.");
            }
        }
    }
}

class WiseSaying {
    int id;
    String author;
    String content;

    WiseSaying(int id, String author, String content) {
        this.id = id;
        this.content = content;
        this.author = author;
    }

    String getSaying() {
        return String.format("%d / %s / %s", id, author, content);
    }
}
