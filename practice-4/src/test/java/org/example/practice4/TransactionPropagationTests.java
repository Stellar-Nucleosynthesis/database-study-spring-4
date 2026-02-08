package org.example.practice4;

import org.example.practice4.entity.Account;
import org.example.practice4.repository.AccountRepository;
import org.example.practice4.service.OuterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionPropagationTests {
    @Autowired
    AccountRepository repo;
    @Autowired
    OuterService outerService;

    @Test
    void propagation_required_rollsEverythingBack() {
        repo.deleteAll();

        assertThrows(RuntimeException.class, () -> outerService.outerRequired("outR", "inR"));

        List<Account> all = repo.findAll();
        assertTrue(all.isEmpty(), "Expected rollback of both outer and inner in REQUIRED case");
    }

    @Test
    void propagation_requiresNew_innerRollback_outerCommits() {
        repo.deleteAll();

        outerService.outerRequiresNew("outNew", "inNew");

        List<Account> all = repo.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().anyMatch(a -> "outNew".equals(a.getName())));
        assertFalse(all.stream().anyMatch(a -> "inNew".equals(a.getName())));
    }

    @Test
    void propagation_nested_innerRollback_outerCommits() {
        repo.deleteAll();

        outerService.outerNested("outNest", "inNest");

        List<Account> all = repo.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().anyMatch(a -> "outNest".equals(a.getName())));
        assertFalse(all.stream().anyMatch(a -> "inNest".equals(a.getName())));
    }
}