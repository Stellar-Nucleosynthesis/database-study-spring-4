package org.example.practice6.repository;

import org.example.practice6.entities.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String> {
    List<Book> findByTitle(String title);
    Optional<Book> findByIsbn(String isbn);
    List<Book> findByPublisher(String publisher);

    @Query("{ 'title' : ?0 }")
    List<Book> findByTheBooksTitle(String title);

    @Query("{ 'authorName' : ?0 }")
    List<Book> findByAuthorNameQuery(String authorName);

    @Query("{ 'publisher' : { $regex: ?0, $options: 'i' } }")
    List<Book> findByPublisherRegex(String publisherRegex);
}