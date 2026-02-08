package org.example.practice4;

import org.example.practice4.exception.CheckedException;
import org.example.practice4.repository.AccountRepository;
import org.example.practice4.service.RollbackService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class RollbackServiceTests {
    @Autowired
    RollbackService rollbackService;

    @Autowired
    AccountRepository accountRepository;

    @Test
    void checkedException_shouldRollbackTransaction() {
        long before = accountRepository.count();

        assertThrows(CheckedException.class,
                () -> rollbackService.checkedRollback());

        long after = accountRepository.count();
        assertEquals(
                before,
                after,
                "Checked exception must cause transaction rollback"
        );
    }

    @Test
    void runtimeException_shouldNotRollbackTransaction() {
        long before = accountRepository.count();

        assertThrows(RuntimeException.class,
                () -> rollbackService.runtimeNoRollback());

        long after = accountRepository.count();
        assertEquals(
                before + 1,
                after,
                "RuntimeException with noRollbackFor must NOT rollback transaction"
        );
    }
}