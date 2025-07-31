package domain.wiseSaying.entity;

public class WiseSaying {
    private int id;
    private String author;
    private String content;

    public WiseSaying(int id, String author, String content) {
        this.id = id;
        this.content = content;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String toListFormat() {
        return String.format("%d / %s / %s", id, author, content);
    }

    public String toJson() {
        return String.format("{\n\t\"id\": %d,\n\t\"content\": \"%s\",\n\t\"author\": \"%s\"\n}", id, content, author);
    }
}
