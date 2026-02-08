package org.example.practice4.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.example.practice4.entity.Account;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceEntityManager implements AccountService {
    private final EntityManagerFactory emf;

    public Optional<Account> findById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            Account a = em.find(Account.class, id);
            return Optional.ofNullable(a);
        }
    }

    public Account create(String name) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Account a = new Account(null, name, BigDecimal.ZERO);
            em.persist(a);
            em.getTransaction().commit();
            return a;
        } finally {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            em.close();
        }
    }

    public Account updateBalance(Long id, BigDecimal delta) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Account a = em.find(Account.class, id);
            if (a == null) throw new IllegalArgumentException("Not found " + id);
            a = new Account(a.getId(), a.getName(), a.getBalance().add(delta));
            Account merged = em.merge(a);
            em.getTransaction().commit();
            return merged;
        } finally {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Account a = em.find(Account.class, id);
            if (a != null) em.remove(a);
            em.getTransaction().commit();
        } finally {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            em.close();
        }
    }
}
