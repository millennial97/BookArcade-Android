package in.bookarcade.app.model;

public class CarouselItem {

    private String image_url;
    private String item_type;
    private String path;

    public CarouselItem(String image_url, String item_type, String path) {
        this.image_url = image_url;
        this.item_type = item_type;
        this.path = path;
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
