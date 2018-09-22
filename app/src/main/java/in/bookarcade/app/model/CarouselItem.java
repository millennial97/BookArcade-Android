package in.bookarcade.app.model;

public class CarouselItem {

    private String image_url;
    private String item_type;
    private String path;
    private String author, book_id, m_image_url, title;
    private double mrp, price;

    public CarouselItem(String image_url, String item_type, String path, String author, String book_id, String m_image_url, String title, double mrp, double price) {
        this.image_url = image_url;
        this.item_type = item_type;
        this.path = path;
        this.author = author;
        this.book_id = book_id;
        this.m_image_url = m_image_url;
        this.title = title;
        this.mrp = mrp;
        this.price = price;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBookId() {
        return book_id;
    }

    public void setBookId(String book_id) {
        this.book_id = book_id;
    }

    public String getMImageUrl() {
        return m_image_url;
    }

    public void setMImageUrl(String m_image_url) {
        this.m_image_url = m_image_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getMrp() {
        return mrp;
    }

    public void setMrp(double mrp) {
        this.mrp = mrp;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getItemType() {
        return item_type;
    }

    public void setItemType(String item_type) {
        this.item_type = item_type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getImageUrl() {
        return image_url;
    }

    public void setImageUrl(String image_url) {
        this.image_url = image_url;
    }
}
