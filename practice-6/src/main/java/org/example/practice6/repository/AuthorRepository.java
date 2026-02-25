package org.example.practice6.repository;

import org.example.practice6.entities.Author;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends MongoRepository<Author, String> {
    List<Author> findByName(String name);
    Optional<Author> findByEmail(String email);
    List<Author> findByNameContaining(String fragment);

    @Query("{ 'name' : ?0 }")
    List<Author> findAuthorsByNameQuery(String name);

    @Query("{ 'email' : { $regex: ?0, $options: 'i' } }")
    List<Author> findAuthorsByEmailRegex(String emailRegex);
}