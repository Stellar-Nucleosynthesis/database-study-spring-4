package org.example.practice2.repository;

import jakarta.persistence.EntityManager;
import org.example.practice2.entities.PersonFull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class PersonFullRepositoryImplTest {
    @Autowired
    private PersonFullRepositoryImpl repository;

    @Autowired
    private EntityManager em;

    @Test
    void findExisting_shouldReturnPerson() {
        PersonFull p = repository.find(1L);
        assertNotNull(p);
        assertEquals("Ivan", p.getFirstName());
    }

    @Test
    void persist_shouldGenerateId_andBeFindable() {
        PersonFull p = new PersonFull();
        p.setFirstName("Test");
        p.setLastName("Test2");
        p.setFullName("Test Test2");
        p.setSalaryRounded(1000L);

        repository.persist(p);
        em.flush();
        assertNotNull(p.getId());

        Long generatedId = p.getId();
        PersonFull found = repository.find(generatedId);
        assertNotNull(found);
        assertEquals("Test", found.getFirstName());
        assertEquals("Test Test2", found.getFullName());
    }

    @Test
    void merge_shouldUpdateEntity() {
        PersonFull p = repository.find(1L);
        assertNotNull(p);

        p.setFirstName("New");
        PersonFull merged = repository.merge(p);
        assertNotNull(merged);
        em.flush();
        em.clear();

        PersonFull reloaded = repository.find(1L);
        assertNotNull(reloaded);
        assertEquals("New", reloaded.getFirstName());
    }

    @Test
    void remove_shouldDeleteEntity() {
        Long id = 1L;
        PersonFull p = repository.find(id);

        repository.remove(p);
        em.flush();
        em.clear();

        PersonFull after = repository.find(id);
        assertNull(after);
    }

    @Test
    void detach_shouldPreventAutomaticSync() {
        Long id = 1L;
        PersonFull p = repository.find(id);
        String beforeDetach = p.getFirstName();
        repository.detach(p);

        p.setFirstName("AfterDetachedChange");
        em.flush();
        em.clear();

        PersonFull fromDb = repository.find(id);
        assertNotNull(fromDb);
        assertEquals(beforeDetach, fromDb.getFirstName());
    }

    @Test
    void refresh_shouldReloadStateFromDatabase() {
        PersonFull managed = repository.find(2L);
        assertNotNull(managed);

        em.createNativeQuery("UPDATE person_full SET first_name = :name WHERE id = :id")
                .setParameter("name", "New")
                .setParameter("id", 2L)
                .executeUpdate();
        em.flush();

        repository.refresh(managed);
        assertEquals("New", managed.getFirstName());
    }
}
