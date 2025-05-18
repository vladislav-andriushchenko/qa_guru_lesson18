package models;

import lombok.Data;

import java.util.List;

@Data
public class AllBooksResponseModel {
    private List<BookModel> books;
}
