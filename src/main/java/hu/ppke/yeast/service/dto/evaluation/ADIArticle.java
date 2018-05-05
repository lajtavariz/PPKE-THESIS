package hu.ppke.yeast.service.dto.evaluation;

public class ADIArticle {
    private int id;
    private String title;
    private String content;

    public int getId() {
        return id;
    }

    public ADIArticle setId(int id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ADIArticle setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ADIArticle setContent(String content) {
        this.content = content;
        return this;
    }
}
