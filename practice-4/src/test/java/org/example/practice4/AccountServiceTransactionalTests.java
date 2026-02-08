package org.example.practice4;

import org.example.practice4.service.AccountService;
import org.example.practice4.service.AccountServiceTransactional;
import org.springframework.beans.factory.annotation.Autowired;

public class AccountServiceTransactionalTests extends AccountServiceAbstractTests{
    @Autowired
    private AccountServiceTransactional  accountServiceTransactional;

    @Override
    protected AccountService service() {
        return accountServiceTransactional;
    }
}
