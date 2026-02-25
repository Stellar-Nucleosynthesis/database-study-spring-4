package org.example.practice6.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "authors")
@Data
public class Author {
    @Id
    private String id;
    private String name;
    private String email;

    public Author(String name, String email) {
        this.name = name;
        this.email = email;
    }
}