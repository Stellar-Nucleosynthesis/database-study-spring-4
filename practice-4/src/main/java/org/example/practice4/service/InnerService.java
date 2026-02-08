package org.example.practice4.service;


import lombok.RequiredArgsConstructor;
import org.example.practice4.entity.Account;
import org.example.practice4.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class InnerService {
    private final AccountRepository repo;

    @Transactional(propagation = Propagation.REQUIRED)
    public void requiredSaveThenFail(String innerName) {
        repo.save(new Account(null, innerName, BigDecimal.ZERO));
        throw new RuntimeException("inner required fail");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void requiresNewSaveThenFail(String innerName) {
        repo.save(new Account(null, innerName, BigDecimal.ZERO));
        throw new RuntimeException("inner requires_new fail");
    }

    @Transactional(propagation = Propagation.NESTED)
    public void nestedSaveThenFail(String innerName) {
        repo.save(new Account(null, innerName, BigDecimal.ZERO));
        throw new RuntimeException("inner nested fail");
    }
}