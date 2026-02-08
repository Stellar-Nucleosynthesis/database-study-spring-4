package org.example.practice4.service;

import lombok.RequiredArgsConstructor;
import org.example.practice4.entity.Account;
import org.example.practice4.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceTxTemplate implements AccountService {
    private final AccountRepository repo;
    private final TransactionTemplate tpl;

    public Optional<Account> findById(Long id) {
        return repo.findById(id);
    }

    public Account create(String name) {
        return tpl.execute(status -> repo.save(new Account(null, name, BigDecimal.ZERO)));
    }

    public Account updateBalance(Long id, BigDecimal delta) {
        return tpl.execute(status -> {
            Account a = repo.findById(id).orElseThrow();
            Account updated = new Account(a.getId(), a.getName(), a.getBalance().add(delta));
            return repo.save(updated);
        });
    }

    public void delete(Long id) {
        tpl.executeWithoutResult(status -> repo.deleteById(id));
    }
}
