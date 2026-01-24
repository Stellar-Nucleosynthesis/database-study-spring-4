package org.example.practice2.repository;

import org.example.practice2.entities.PersonFull;

public interface PersonFullRepository {
    void persist(PersonFull person);
    void detach(PersonFull person);
    void remove(PersonFull person);
    void refresh(PersonFull person);
    PersonFull merge(PersonFull person);
    PersonFull find(Long id);
}
