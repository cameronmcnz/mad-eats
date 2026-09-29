package com.eatrading.api.objects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusTest {

    @Test
    void testEnumContainsExpectedValues() {
        Status[] values = Status.values();
        assertNotNull(values);
        assertTrue(values.length >= 4);
        assertEquals(Status.SUBMITTED, Status.valueOf("SUBMITTED"));
        assertEquals(Status.REJECTED, Status.valueOf("REJECTED"));
        assertEquals(Status.FILLED, Status.valueOf("FILLED"));
        assertEquals(Status.ACCEPTED, Status.valueOf("ACCEPTED"));
    }
}
