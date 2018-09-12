package in.bookarcade.app.model;

import java.util.List;

public class Section {

    private String section_name;
    private String section_id;
    private List<HomeBook> books;

    public Section(String section_name, String section_id, List<HomeBook> books) {
        this.section_name = section_name;
        this.section_id = section_id;
        this.books = books;
    }

    public String getSectionName() {
        return section_name;
    }

    public void setSectionName(String section_name) {
        this.section_name = section_name;
    }

    public String getSectionId() {
        return section_id;
    }

    public void setSectionId(String section_id) {
        this.section_id = section_id;
    }

    public List<HomeBook> getBooks() {
        return books;
    }

    public void setBooks(List<HomeBook> books) {
        this.books = books;
    }
}
