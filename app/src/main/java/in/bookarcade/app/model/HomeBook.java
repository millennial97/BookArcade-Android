package in.bookarcade.app.model;

public class HomeBook {

    private String book_title;
    private String book_id;
    private String image_url;
    private String book_author;
    private double book_mrp;
    private double book_price;
    private String s_image_url;

    public HomeBook(String book_title, String book_id, String image_url, String book_author, double book_mrp, double book_price, String s_image_url) {
        this.book_title = book_title;
        this.book_id = book_id;
        this.image_url = image_url;
        this.book_author = book_author;
        this.book_mrp = book_mrp;
        this.s_image_url = s_image_url;
        this.book_price = book_price;
    }

    public String getSImageUrl() {
        return s_image_url;
    }

    public void setSImageUrl(String s_image_url) {
        this.s_image_url = s_image_url;
    }

    public String getBookTitle() {
        return book_title;
    }

    public void setBookTitle(String book_title) {
        this.book_title = book_title;
    }

    public String getBookId() {
        return book_id;
    }

    public void setBookId(String book_id) {
        this.book_id = book_id;
    }

    public String getImageUrl() {
        return image_url;
    }

    public void setImageUrl(String image_url) {
        this.image_url = image_url;
    }

    public String getBookAuthor() {
        return book_author;
    }

    public void setBookAuthor(String book_author) {
        this.book_author = book_author;
    }

    public double getBookMrp() {
        return book_mrp;
    }

    public void setBookMrp(double book_mrp) {
        this.book_mrp = book_mrp;
    }

    public double getBookPrice() {
        return book_price;
    }

    public void setBookPrice(double book_price) {
        this.book_price = book_price;
    }
}
