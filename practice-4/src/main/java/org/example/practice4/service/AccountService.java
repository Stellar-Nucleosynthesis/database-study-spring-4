package org.example.practice4.service;

import org.example.practice4.entity.Account;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountService {
    Optional<Account> findById(Long id);
    Account updateBalance(Long id, BigDecimal delta);
    Account create(String name);
    void delete(Long id);
}
