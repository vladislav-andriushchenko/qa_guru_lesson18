package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BookModel {
    String isbn;
    String subTitle;
    String title;
    String author;
    @JsonProperty("publish_date")
    String publish_date;
    String publisher;
    Integer pages;
    String description;
    String website;
}
