import domain.Rq;
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
            String input = sc.nextLine().trim();

            Rq rq = new Rq(input);
            String command = rq.getActionName();

            //시스템 명령
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
                    wiseSayingController.delete(rq);
                    break;
                case "수정" :
                    wiseSayingController.update(rq);
                    break;
                case "빌드" :
                    wiseSayingController.build();
                    break;
                default :
                    System.out.println("다시 입력해주세요.");
            }
        }
    }
}
