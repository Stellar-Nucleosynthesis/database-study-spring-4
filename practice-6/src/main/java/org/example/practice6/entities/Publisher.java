package org.example.practice6.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "publishers")
@Data
public class Publisher {
    @Id
    private String id;
    private String name;
    private String country;

    public Publisher(String name, String country) {
        this.name = name;
        this.country = country;
    }
}