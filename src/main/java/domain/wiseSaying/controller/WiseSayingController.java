package domain.wiseSaying.controller;

import domain.Rq;
import domain.wiseSaying.entity.WiseSaying;
import domain.wiseSaying.service.WiseSayingService;
import domain.Rq;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * 역할 : 명언에 관려된 응대, 고객의 명렁을 입력받고 적절한 응답을 표현
 * 스캐너, 출력 사용 가능
 */
public class WiseSayingController {

    private final Scanner sc;
    private final WiseSayingService service;

    public WiseSayingController(Scanner sc) {
        this.sc = sc;
        this.service = new WiseSayingService();
    }
    //등록
    public void register() {
        //명언 입력
        System.out.print("명언 : ");
        String content = sc.nextLine();

        //작가 입력
        System.out.print("작가 : ");
        String author = sc.nextLine();

        //저장
        int id = service.register(author, content);

        System.out.printf("%d번 명언이 등록되었습니다.\n", id);
    }

    //목록
    public void list() {
        List<WiseSaying> list = service.getList();
        String result = list.stream()
                .map(WiseSaying::toListFormat)
                .collect(Collectors.joining("\n"));

        //출력
        System.out.println(result);
    }

    //삭제
    public void delete(domain.Rq rq) {
        int id = rq.getParamAsInt("id", -1);
        if(id == -1) {
            System.out.println("다시 입력해주세요.");
            return;
        }
        if(service.delete(id)) {
            System.out.printf("%d번 명언이 삭제되었습니다.\n", id);
        } else {
            System.out.printf("%d번 명언은 존재하지 않습니다.\n", id);
        }
    }

    //수정
    public void update(domain.Rq rq) {
        int id = rq.getParamAsInt("id", -1);
        if (id == -1) {
            System.out.println("id를 확인해주세요.");
            return;
        }

        WiseSaying target = service.findById(id);
        if (target == null) {
            System.out.printf("%d번 명언은 존재하지 않습니다.\n", id);
            return;
        }

        System.out.printf("명언(기존) : %s\n", target.getContent());
        System.out.print("명언 : ");
        String newContent = sc.nextLine();

        System.out.printf("작가(기존) : %s\n", target.getAuthor());
        System.out.print("작가 : ");
        String newAuthor = sc.nextLine();

        service.update(id, newAuthor, newContent);
    }

    //빌드
    public void build() {
        if(service.buildJson()) System.out.println("data.json 파일의 내용이 갱신되었습니다.");
    }

}
