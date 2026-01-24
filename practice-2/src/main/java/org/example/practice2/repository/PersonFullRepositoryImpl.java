package org.example.practice2.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.example.practice2.entities.PersonFull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersonFullRepositoryImpl implements PersonFullRepository {
    private final EntityManager em;

    @Override
    public void persist(PersonFull person) {
        em.persist(person);
    }

    @Override
    public void detach(PersonFull person) {
        em.detach(person);
    }

    @Override
    public void remove(PersonFull person) {
        em.remove(person);
    }

    @Override
    public void refresh(PersonFull person) {
        em.refresh(person);
    }

    @Override
    public PersonFull merge(PersonFull person) {
        return em.merge(person);
    }

    @Override
    public PersonFull find(Long id) {
        return em.find(PersonFull.class, id);
    }
}
