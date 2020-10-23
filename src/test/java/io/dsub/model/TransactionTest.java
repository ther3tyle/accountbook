package io.dsub.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Random;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    Transaction transaction;

    @BeforeEach
    void prep() {
        Random random = new Random();
        transaction = new Transaction(random.nextInt(1000), random.nextInt(1000));
    }

    @Test
    void getParser() {
        Function<String, Model> parser = Transaction.getParser();
        assertDoesNotThrow(() -> {
            String modelString = transaction.toString();
            assertEquals(transaction, parser.apply(modelString));
        });
    }

    @Test
    void withAmount() {
        long amount = new Random().nextLong();
        Transaction oldTransaction = transaction;
        transaction = oldTransaction.withAmount(amount);
        assertNotEquals(oldTransaction, transaction);
        assertNotEquals(amount, oldTransaction.getAmount());
        assertEquals(amount, transaction.getAmount());
    }

    @Test
    void withVendorId() {
        int id = new Random().nextInt(3003);
        Transaction old = transaction;
        transaction = old.withVendorId(id);
        assertNotEquals(old, transaction);
        assertNotEquals(id, old.getVendorId());
        assertEquals(id, transaction.getVendorId());
    }

    @Test
    void withTime() {
        LocalDate date = LocalDate.now().minusWeeks(3L).minusDays(3);
        Transaction old = transaction;
        transaction = old.withDate(date);
        assertNotEquals(old, transaction);
        assertNotEquals(date, old.getDate());
        assertEquals(date, transaction.getDate());
    }

    @Test
    void getAmount() {
        Transaction old = transaction;
        long val = new Random().nextLong();
        transaction = old.withAmount(val);
        assertNotEquals(old, transaction);
        assertNotEquals(val, old.getAmount());
        assertEquals(val, transaction.getAmount());
    }

    @Test
    void testEquals() {
        Transaction other = transaction.withAmount(3L);
        assertNotEquals(this.transaction, other);
        other = other.withAmount(this.transaction.getAmount());
        assertEquals(this.transaction, other);

        other = new Transaction(transaction.getAmount(), transaction.getVendorId());
        other = other.withDate(transaction.getDate());
        assertNotEquals(this.transaction, other);
    }
}