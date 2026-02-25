package org.example.practice6.service;

import lombok.RequiredArgsConstructor;
import org.example.practice6.entities.Author;
import org.example.practice6.entities.Book;
import org.example.practice6.entities.Publisher;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TemplateQueryService {
    private final MongoTemplate mongoTemplate;

    public List<Book> findBooksByTitleLike(String fragment) {
        Query q = new Query();
        q.addCriteria(Criteria.where("title").regex(fragment, "i"));
        return mongoTemplate.find(q, Book.class);
    }

    public List<Author> findAuthorsByEmailDomain(String domainRegex) {
        Query q = new Query();
        q.addCriteria(Criteria.where("email").regex(domainRegex, "i"));
        return mongoTemplate.find(q, Author.class);
    }

    public List<Publisher> findPublishersByCountrySorted(String country) {
        Query q = new Query();
        q.addCriteria(Criteria.where("country").is(country));
        q.with(Sort.by(Sort.Direction.ASC, "name"));
        return mongoTemplate.find(q, Publisher.class);
    }
}