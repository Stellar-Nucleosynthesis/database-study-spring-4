package org.example.practice4;

import org.example.practice4.service.AccountService;
import org.example.practice4.service.AccountServiceEntityManager;
import org.springframework.beans.factory.annotation.Autowired;

public class AccountServiceEntityManagerTests extends AccountServiceAbstractTests{
    @Autowired
    private AccountServiceEntityManager accountServiceEntityManager;

    @Override
    protected AccountService service() {
        return accountServiceEntityManager;
    }
}
