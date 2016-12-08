package com.readlearncode.dukesbookstore.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Source code github.com/readlearncode
 *
 * @author Alex Theedom www.readlearncode.com
 * @version 1.0
 */
public class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(min = 10, max = 10)
    private String id;

    @NotNull
    @Size(min = 1)
    private String title;

    @NotNull
    private String description;

    @NotNull
    private List<String> authors;

    @NotNull
    private Float price;

    private String imageFileName;

    @NotNull
    private String link;

    @NotNull
    private String published;

    public Book(){}

    public Book(String id, String title, String description, Float price, String published, List<String> authors, String imageFileName, String link) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.published = published;
        this.authors = authors;
        this.imageFileName = imageFileName;
        this.link = link;
    }

    public final String getId() {
        return id;
    }

    public final void setId(final String id) {
        this.id = id;
    }

    public final String getTitle() {
        return title;
    }

    public final void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public final Float getPrice() {
        return price;
    }

    public final void setPrice(final Float price) {
        this.price = price;
    }

    public final String getPublished() {
        return published;
    }

    public final void setPublished(final String published) {
        this.published = published;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) &&
                Objects.equals(title, book.title) &&
                Objects.equals(description, book.description) &&
                Objects.equals(authors, book.authors) &&
                Objects.equals(price, book.price) &&
                Objects.equals(imageFileName, book.imageFileName) &&
                Objects.equals(link, book.link) &&
                Objects.equals(published, book.published);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, authors, price, imageFileName, link, published);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", authors=" + authors +
                ", price=" + price +
                ", imageFileName='" + imageFileName + '\'' +
                ", link='" + link + '\'' +
                ", published='" + published + '\'' +
                '}';
    }
}