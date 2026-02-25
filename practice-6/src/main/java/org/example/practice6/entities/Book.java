package org.example.practice6.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "books")
@Data
public class Book {
    @Id
    private String id;

    @Indexed(unique = true)
    private String isbn;

    private String title;
    private String authorName;
    private String publisher;

    public Book(String isbn, String title, String authorName, String publisher) {
        this.isbn = isbn;
        this.title = title;
        this.authorName = authorName;
        this.publisher = publisher;
    }
}