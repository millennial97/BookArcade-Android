package in.bookarcade.app.model;

public class HomeAuthor {

    private String author_name;
    private String image_url;
    private String author_id;


    public HomeAuthor(String author_name, String image_url, String author_id) {
        this.author_name = author_name;
        this.image_url = image_url;
        this.author_id = author_id;
    }

    public String getAuthorName() {
        return author_name;
    }

    public void setAuthorName(String author_name) {
        this.author_name = author_name;
    }

    public String getImageUrl() {
        return image_url;
    }

    public void setImageUrl(String image_url) {
        this.image_url = image_url;
    }

    public String getAuthorId() {
        return author_id;
    }

    public void setAuthorId(String author_id) {
        this.author_id = author_id;
    }
}
