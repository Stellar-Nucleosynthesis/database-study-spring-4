package org.example.practice6;

import org.example.practice6.entities.Author;
import org.example.practice6.entities.Book;
import org.example.practice6.entities.Publisher;
import org.example.practice6.repository.AuthorRepository;
import org.example.practice6.repository.BookRepository;
import org.example.practice6.repository.PublisherRepository;
import org.example.practice6.service.TemplateQueryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.IndexInfo;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class BookStoreIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer =
            new MongoDBContainer("mongo:7.0");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.data.mongodb.auto-index-creation", () -> true);
    }

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    PublisherRepository publisherRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    TemplateQueryService templateQueryService;

    @AfterEach
    void cleanup() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        publisherRepository.deleteAll();
    }

    @Test
    void uniqueIndex_prevents_duplicate_isbn() {
        bookRepository.save(new Book("ISBN-123", "Title A", "Ivan Ivanov", "Pub1"));

        Book duplicate = new Book("ISBN-123", "Title B", "Petro Petrenko", "Pub2");

        assertThrows(DuplicateKeyException.class, () -> bookRepository.save(duplicate));
    }

    @Test
    void indexExists_isbn_index_is_present() {
        bookRepository.save(new Book("X-1", "Some", "A", "P"));

        List<IndexInfo> indexes = mongoTemplate.indexOps(Book.class).getIndexInfo();

        boolean found = indexes.stream()
                .flatMap(i -> i.getIndexFields().stream())
                .anyMatch(f -> "isbn".equals(f.getKey()));

        assertTrue(found, "Expected index on field 'isbn'");
    }

    @Test
    void methodNameQueries_work() {
        bookRepository.save(new Book("ISBN-1", "Alpha", "Ivan", "PubA"));
        bookRepository.save(new Book("ISBN-2", "Beta", "Ivan", "PubB"));

        authorRepository.save(new Author("Ivan", "ivan@example.com"));
        publisherRepository.save(new Publisher("PubA", "Ukraine"));

        List<Book> books = bookRepository.findByTitle("Alpha");
        assertEquals(1, books.size());

        Optional<Author> author = authorRepository.findByEmail("ivan@example.com");
        assertTrue(author.isPresent());
        assertEquals("Ivan", author.get().getName());

        List<Publisher> publishers = publisherRepository.findByCountry("Ukraine");
        assertEquals(1, publishers.size());
        assertEquals("PubA", publishers.getFirst().getName());
    }

    @Test
    void queryAnnotation_methods_work() {
        bookRepository.save(new Book("ISBN-q1", "QueryTitle", "Anna", "QueryPub"));

        List<Book> byTitle = bookRepository.findByTheBooksTitle("QueryTitle");
        assertEquals(1, byTitle.size());

        List<Book> byAuthor = bookRepository.findByAuthorNameQuery("Anna");
        assertEquals(1, byAuthor.size());

        List<Book> byPubRegex = bookRepository.findByPublisherRegex("querypub");
        assertEquals(1, byPubRegex.size());
    }

    @Test
    void mongoTemplate_queries_work() {
        bookRepository.save(new Book("ISBN-t1", "Spring in Action", "Craig", "Manning"));
        authorRepository.save(new Author("Martin", "martin@gmail.com"));
        publisherRepository.save(new Publisher("Packt", "UK"));

        List<Book> books = templateQueryService.findBooksByTitleLike("spring");
        assertEquals(1, books.size());

        List<Author> authors = templateQueryService.findAuthorsByEmailDomain("@gmail.com");
        assertEquals(1, authors.size());

        List<Publisher> pubs = templateQueryService.findPublishersByCountrySorted("UK");
        assertEquals(1, pubs.size());
        assertEquals("Packt", pubs.getFirst().getName());
    }
}