package org.example.practice4.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProxyInvocationService {
    private final SelfInvocationService target;

    public boolean callViaProxy() {
        return target.transactionalMethod();
    }
}