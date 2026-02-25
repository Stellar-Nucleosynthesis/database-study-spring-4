package org.example.practice6.repository;

import org.example.practice6.entities.Publisher;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PublisherRepository extends MongoRepository<Publisher, String> {
    List<Publisher> findByName(String name);
    List<Publisher> findByCountry(String country);
    List<Publisher> findByNameContaining(String fragment);

    @Query("{ 'country' : ?0 }")
    List<Publisher> findByCountryQuery(String country);

    @Query("{ 'name' : { $regex: ?0, $options: 'i' } }")
    List<Publisher> findByNameRegex(String nameRegex);
}