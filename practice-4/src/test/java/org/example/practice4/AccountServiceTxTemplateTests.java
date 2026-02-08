package org.example.practice4;

import org.example.practice4.service.AccountService;
import org.example.practice4.service.AccountServiceTxTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class AccountServiceTxTemplateTests extends AccountServiceAbstractTests {
    @Autowired
    private AccountServiceTxTemplate accountServiceTxTemplate;

    @Override
    protected AccountService service() {
        return accountServiceTxTemplate;
    }
}
