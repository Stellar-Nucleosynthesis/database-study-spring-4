package org.example.practice4.service;

import lombok.RequiredArgsConstructor;
import org.example.practice4.entity.Account;
import org.example.practice4.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceTransactional implements AccountService {
    private final AccountRepository repo;

    public Optional<Account> findById(Long id) {
        return repo.findById(id);
    }

    @Transactional
    public Account create(String name) {
        return repo.save(new Account(null, name, BigDecimal.ZERO));
    }

    @Transactional
    public Account updateBalance(Long id, BigDecimal delta) {
        Account a = repo.findById(id).orElseThrow();
        Account updated = new Account(a.getId(), a.getName(), a.getBalance().add(delta));
        return repo.save(updated);
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
