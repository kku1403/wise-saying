package domain.wiseSaying.service;

import domain.wiseSaying.entity.WiseSaying;
import domain.wiseSaying.repository.FileWiseSayingRepository;
import domain.wiseSaying.repository.WiseSayingRepository;

import java.util.Collections;
import java.util.List;

/**
 * 순수 비즈니스 로직
 * 스캐너, 출력 금지
 */
public class WiseSayingService {

    private final WiseSayingRepository repo;
    private final int limit = 5;

    public WiseSayingService() {
        this.repo = new FileWiseSayingRepository();
    }

    public int register(String author, String content) {
        return repo.save(author, content);
    }

    public boolean delete(int id) {
        return repo.delete(id);
    }

    //페이지에 속한 명언 list 반환
    public List<WiseSaying> getPagedList(int page, String keywordType, String keyword) {
        List<WiseSaying> all = repo.getList(keywordType, keyword);
        int fromIndex = (page-1)*limit;
        int toIndex = Math.min(fromIndex + limit, all.size());

        if(fromIndex >= all.size()) {
            return Collections.emptyList();
        }
        return all.subList(fromIndex, toIndex);
    }

    //전체 페이지 수 반환
    public int getTotalPages(String keywordType, String keyword) {
        int totalCount = repo.getList(keywordType, keyword).size();
        return (int) Math.ceil((double) totalCount/limit);
    }

    public void update(int id, String newAuthor, String newContent) {
        repo.update(id, newAuthor, newContent);
    }

    public boolean buildJson() {
        return repo.buildJsonFile();
    }

    public WiseSaying findById(int id) {
        return repo.findById(id);
    }

    public void generateSampleDataIfEmpty() {
        if(repo.getList("","").isEmpty()) {
            for(int i=1; i<=10; i++) {
                repo.save("작자미상" + i, "명언" + i);
            }
        }
    }
}
