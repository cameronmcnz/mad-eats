package com.eatrading.api.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Instant;

/**
 * Unit tests for User class.
 */

class ConcreteUser extends User {
    public ConcreteUser() { super(); }
    public ConcreteUser(String name, String email) { super(name, email); }
}

public class UserTest {
    private ConcreteUser user;

    @BeforeEach
    void setUp() {
        user = new ConcreteUser("Alice", "a@example.com");
    }

    @Test
    void constructorSetsIdNameEmailAndTimestamps() {
        ConcreteUser u = new ConcreteUser("Alice", "a@example.com");
        assertNotNull(u.getId());
        assertEquals("Alice", u.getName());
        assertEquals("a@example.com", u.getEmail());
        assertNotNull(u.getCreatedAt());
        assertNotNull(u.getUpdatedAt());
    }

    @Test
    void gettersReturnExpectedValues() {
        assertEquals("Alice", user.getName());
        assertEquals("a@example.com", user.getEmail());
    }

    @Test
    void settersUpdateNameAndEmail() {
        user.setName("Bob");
        user.setEmail("b@example.com");
        assertEquals("Bob", user.getName());
        assertEquals("b@example.com", user.getEmail());
    }

    @Test
    void createdAtSetterWorks() {
        Instant now = Instant.now();
        user.setCreatedAt(now);
        assertEquals(now, user.getCreatedAt());
    }

    @Test
    void updatedAtSetterWorks() {
        Instant now = Instant.now();
        user.setUpdatedAt(now);
        assertEquals(now, user.getUpdatedAt());
    }
}
