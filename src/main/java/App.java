import domain.system.controller.SystemController;
import domain.wiseSaying.controller.WiseSayingController;

import java.util.*;

/**
 * 역할 : 사용자 입력을 받고 그것이 WiseSayingController에게 넘기기
 * 스캐너, 출력 사용 가능
 */
public class App {

    private final Scanner sc;
    private final WiseSayingController wiseSayingController;
    private final SystemController systemController;

    public App() {
        this.sc = new Scanner(System.in);
        this.wiseSayingController = new WiseSayingController(sc);
        this.systemController = new SystemController();
    }
    public void run() {

        System.out.println("== 명언 앱 ==");

        while(true) {
            //명령 입력
            System.out.print("명령) ");
            String[] input = sc.nextLine().trim().split("\\?id=");
            String command = input[0];

            //시스템 명령
            //명령 == 종료
            if(command.equals("종료")) {
                systemController.exit();
                break;
            }

            //명언 명령
            switch (command) {
                case "등록" :
                    wiseSayingController.register();
                    break;
                case "목록" :
                    wiseSayingController.list();
                    break;
                case "삭제" :
                    wiseSayingController.delete(input);
                    break;
                case "수정" :
                    wiseSayingController.update(input);
                    break;
                case "빌드" :
                    wiseSayingController.build();
                    break;
                default :
                    System.out.println("다시 입력해주세요.");
            }
//            //명령 == 등록
//            else if(command.equals("등록")) {
//                handleRegister();
//            }
//
//            //명령 == 목록
//            else if(command.equals("목록")) {
//                handleList();
//            }
//
//            //명령 == 삭제
//            else if(command.equals("삭제")) {
//                try {
//                    int targetId = Integer.parseInt(input[1]);
//                    handleDelete(targetId);
//                }
//                catch(Exception e){
//                    System.out.println("다시 입력해주세요.");
//                }
//            }
//
//            //명령 == 수정
//            else if(command.equals("수정")) {
//                try {
//                    int targetId = Integer.parseInt(input[1]);
//                    handleUpdate(targetId);
//                }
//                catch(Exception e){
//                    System.out.println("다시 입력해주세요.");
//                }
//            }
//
//            //명령 == 빌드
//            else if(command.equals("빌드")) {
//                repo.buildJsonFile();
//            }
//
//            //그 외
//            else {
//                System.out.println("다시 입력해주세요.");
//            }
        }
    }
}
