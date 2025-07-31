package domain.wiseSaying.service;

import domain.wiseSaying.entity.WiseSaying;
import domain.wiseSaying.repository.FileWiseSayingRepository;
import domain.wiseSaying.repository.WiseSayingRepository;

import java.util.List;

/**
 * 순수 비즈니스 로직
 * 스캐너, 출력 금지
 */
public class WiseSayingService {

    private final WiseSayingRepository repo;

    public WiseSayingService() {
        this.repo = new FileWiseSayingRepository();
    }

    public int register(String author, String content) {
        return repo.save(author, content);
    }

    public boolean delete(int id) {
        return repo.delete(id);
    }

    public List<WiseSaying> getList(String keywordType, String keyword) {
        return repo.getList(keywordType, keyword);
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
}
