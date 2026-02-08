package org.example.practice4.service;

import lombok.RequiredArgsConstructor;
import org.example.practice4.entity.Account;
import org.example.practice4.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OuterService {
    private final InnerService inner;
    private final AccountRepository repo;

    @Transactional
    public void outerRequired(String outerName, String innerName) {
        repo.save(new Account(null, outerName, BigDecimal.ZERO));
        inner.requiredSaveThenFail(innerName);
    }

    @Transactional
    public void outerRequiresNew(String outerName, String innerName) {
        repo.save(new Account(null, outerName, BigDecimal.ZERO));
        try {
            inner.requiresNewSaveThenFail(innerName);
        } catch (Exception ignored) {
            // inner rolled back; outer continues
        }
    }

    @Transactional
    public void outerNested(String outerName, String innerName) {
        repo.save(new Account(null, outerName, BigDecimal.ZERO));
        try {
            inner.nestedSaveThenFail(innerName);
        } catch (Exception ignored) {
            // nested rolled back to savepoint
        }
    }
}