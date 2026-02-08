package org.example.practice4;

import org.example.practice4.entity.Account;
import org.example.practice4.repository.AccountRepository;
import org.example.practice4.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
abstract class AccountServiceAbstractTests {
    @Autowired
    protected AccountRepository repository;

    protected abstract AccountService service();

    @BeforeEach
    void cleanDb() {
        repository.deleteAll();
    }

    @Test
    void create_shouldPersistAccount() {
        Account created = service().create("alice");

        assertNotNull(created.getId(), "Created account must have id");
        assertEquals("alice", created.getName());
        assertEquals(BigDecimal.ZERO, created.getBalance());

        Optional<Account> fromDb = repository.findById(created.getId());
        assertTrue(fromDb.isPresent(), "Account must be persisted");
    }

    @Test
    void findById_existingAccount_shouldReturnAccount() {
        Account created = service().create("bob");

        Optional<Account> found = service().findById(created.getId());

        assertTrue(found.isPresent());
        assertEquals("bob", found.get().getName());
    }

    @Test
    void findById_nonExistingAccount_shouldReturnEmpty() {
        Optional<Account> found = service().findById(999L);
        assertTrue(found.isEmpty());
    }

    @Test
    void updateBalance_shouldIncreaseBalance() {
        Account created = service().create("charlie");

        Account updated = service().updateBalance(created.getId(), BigDecimal.TEN);

        assertEquals(0, BigDecimal.TEN.compareTo(updated.getBalance()));

        Account fromDb = repository.findById(created.getId()).orElseThrow();
        assertEquals(0, BigDecimal.TEN.compareTo(fromDb.getBalance()));
    }

    @Test
    void updateBalance_negativeDelta_shouldDecreaseBalance() {
        Account created = service().create("dave");

        Account updated = service().updateBalance(created.getId(), BigDecimal.valueOf(-5));

        assertEquals(0, BigDecimal.valueOf(-5).compareTo(updated.getBalance()));
    }

    @Test
    void updateBalance_nonExistingAccount_shouldFail() {
        assertThrows(RuntimeException.class,
                () -> service().updateBalance(123L, BigDecimal.ONE),
                "Updating non-existing account must fail");
    }

    @Test
    void delete_existingAccount_shouldRemoveIt() {
        Account created = service().create("eve");

        service().delete(created.getId());

        assertFalse(repository.findById(created.getId()).isPresent());
    }
}