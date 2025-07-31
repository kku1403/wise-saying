package domain.wiseSaying.repository;

import domain.wiseSaying.entity.WiseSaying;

import java.util.List;

public interface WiseSayingRepository {
    int save(String author, String content);
    boolean delete(int id);
    void update(int id, String author, String content);
    List<WiseSaying> getList(String keywordType, String keyword);

    WiseSaying findById(int id);

    boolean buildJsonFile();
}
