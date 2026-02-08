package org.example.practice4;

import org.example.practice4.entity.Account;
import org.example.practice4.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class TransactionIsolationTests {
    @Autowired
    AccountRepository repo;
    @Autowired
    PlatformTransactionManager txManager;

    TransactionTemplate serializableTpl;
    TransactionTemplate readCommittedTpl;

    @BeforeEach
    void setup() {
        repo.deleteAll();

        serializableTpl = new TransactionTemplate(txManager);
        serializableTpl.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);

        readCommittedTpl = new TransactionTemplate(txManager);
        readCommittedTpl.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
    }

    @Test
    void serializable_preventsDirtyRead() throws Exception {
        CountDownLatch writeStarted = new CountDownLatch(1);
        CountDownLatch allowRollback = new CountDownLatch(1);

        var pool = Executors.newFixedThreadPool(2);

        Future<?> t1 = pool.submit(() ->
                serializableTpl.executeWithoutResult(status -> {
                    repo.save(new Account(null, "A", BigDecimal.TEN));
                    writeStarted.countDown();
                    await(allowRollback);
                    status.setRollbackOnly();
                })
        );

        Future<BigDecimal> t2 = pool.submit(() -> {
            await(writeStarted);
            return serializableTpl.execute(status ->
                    repo.findAll().stream()
                            .map(Account::getBalance)
                            .findFirst()
                            .orElse(BigDecimal.ZERO)
            );
        });

        allowRollback.countDown();
        t1.get();

        BigDecimal read = t2.get();

        assertThat(read)
                .as("Dirty read must NOT be visible")
                .isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void serializable_preventsNonRepeatableRead() throws Exception {
        Account a = repo.save(new Account(null, "A", BigDecimal.TEN));

        CountDownLatch firstReadDone = new CountDownLatch(1);
        CountDownLatch updateDone = new CountDownLatch(1);

        var pool = Executors.newFixedThreadPool(2);

        Future<List<BigDecimal>> t1 = pool.submit(() ->
                serializableTpl.execute(status -> {
                    BigDecimal first =
                            repo.findById(a.getId()).orElseThrow().getBalance();

                    firstReadDone.countDown();
                    await(updateDone);

                    BigDecimal second =
                            repo.findById(a.getId()).orElseThrow().getBalance();

                    return List.of(first, second);
                })
        );

        Future<?> t2 = pool.submit(() -> {
            await(firstReadDone);
            serializableTpl.executeWithoutResult(status -> {
                Account updated =
                        new Account(a.getId(), "A", BigDecimal.valueOf(99));
                repo.save(updated);
            });
            updateDone.countDown();
        });

        List<BigDecimal> reads = t1.get();
        t2.get();

        assertThat(reads.get(0))
                .isEqualByComparingTo(reads.get(1));
    }

    @Test
    void readCommitted_stillPreventsDirtyRead() throws Exception {
        CountDownLatch writeStarted = new CountDownLatch(1);
        CountDownLatch allowRollback = new CountDownLatch(1);

        var pool = Executors.newFixedThreadPool(2);

        Future<?> t1 = pool.submit(() ->
                readCommittedTpl.executeWithoutResult(status -> {
                    repo.save(new Account(null, "A", BigDecimal.TEN));
                    writeStarted.countDown();
                    await(allowRollback);
                    status.setRollbackOnly();
                })
        );

        Future<BigDecimal> t2 = pool.submit(() -> {
            await(writeStarted);
            return readCommittedTpl.execute(status ->
                    repo.findAll().stream()
                            .map(Account::getBalance)
                            .findFirst()
                            .orElse(BigDecimal.ZERO)
            );
        });

        allowRollback.countDown();
        t1.get();

        BigDecimal read = t2.get();

        assertThat(read)
                .as("Dirty read is prevented even at READ_COMMITTED")
                .isEqualByComparingTo(BigDecimal.ZERO);
    }

    private static void await(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
