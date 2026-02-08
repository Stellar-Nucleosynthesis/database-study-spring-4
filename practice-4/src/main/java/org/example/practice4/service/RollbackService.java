package org.example.practice4.service;

import lombok.RequiredArgsConstructor;
import org.example.practice4.entity.Account;
import org.example.practice4.exception.CheckedException;
import org.example.practice4.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RollbackService {
    private final AccountRepository repo;

    @Transactional(rollbackFor = CheckedException.class)
    public void checkedRollback() throws CheckedException {
        repo.save(new Account(null, "checked", BigDecimal.ONE));
        throw new CheckedException("rollback");
    }

    @Transactional(noRollbackFor = RuntimeException.class)
    public void runtimeNoRollback() {
        repo.save(new Account(null, "runtime", BigDecimal.ONE));
        throw new RuntimeException("no rollback");
    }
}