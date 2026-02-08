package org.example.practice4;


import org.example.practice4.service.ProxyInvocationService;
import org.example.practice4.service.SelfInvocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SelfInvocationServiceTests {
    @Autowired
    SelfInvocationService selfInvocationService;
    @Autowired
    ProxyInvocationService proxyInvocationService;

    @Test
    void selfInvocation_shouldNotOpenTransaction() {
        boolean txActive = selfInvocationService.callInternal();
        assertFalse(
                txActive,
                "Self-invocation must NOT trigger @Transactional proxy"
        );
    }

    @Test
    void proxyInvocation_shouldOpenTransaction() {
        boolean txActive = proxyInvocationService.callViaProxy();
        assertTrue(
                txActive,
                "Invocation via Spring proxy MUST activate transaction"
        );
    }
}
