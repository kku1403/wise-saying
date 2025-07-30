public class WiseSaying {
    int id;
    String author;
    String content;

    public WiseSaying(int id, String author, String content) {
        this.id = id;
        this.content = content;
        this.author = author;
    }

    String getSaying() {
        return String.format("%d / %s / %s", id, author, content);
    }

    String toJson() {
        return String.format("{\n\t\"id\": %d,\n\t\"content\": \"%s\",\n\t\"author\": \"%s\"\n}", id, content, author);
    }
}
