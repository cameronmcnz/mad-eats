package com.eatrading.api.objects;

import com.eatrading.api.entities.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for OrderRequest class.
 * 
 * These tests focus on OrderRequest's behavior:
 * - Construction with an Order object
 * - Retrieving the Order object from the request
 * - Handling null Order objects
 * 
 * MOCKS NEEDED:
 * - Order: Mock the order entity to test OrderRequest functionality without database dependencies
 */
@ExtendWith(MockitoExtension.class)
class OrderRequestTest {

    @Mock private Order mockOrder;

    private OrderRequest orderRequest;

    @BeforeEach
    void setUp() {
        // Setup will be done per test since different tests need different configurations
    }

    // ===== CONSTRUCTOR TESTS =====

    @Test
    void testConstructor_WithValidOrder_StoresOrder() {
        
        // Act
        orderRequest = new OrderRequest(mockOrder);

        // Assert
        assertNotNull(orderRequest, "OrderRequest should be created successfully");
        assertEquals(mockOrder, orderRequest.getOrder(), "OrderRequest should store the provided order");
    }

    @Test
    void testConstructor_WithNullOrder_StoresNull() {
        // Act
        orderRequest = new OrderRequest(null);

        // Assert
        assertNotNull(orderRequest, "OrderRequest should be created even with null order");
        assertNull(orderRequest.getOrder(), "OrderRequest should store null when null order is provided");
    }

    // ===== GETTER TESTS =====

    @Test
    void testGetOrder_ReturnsStoredOrder() {
        // Arrange
        orderRequest = new OrderRequest(mockOrder);

        // Act
        Order retrievedOrder = orderRequest.getOrder();

        // Assert
        assertNotNull(retrievedOrder, "getOrder should return non-null order");
        assertEquals(mockOrder, retrievedOrder, "getOrder should return the exact order stored in constructor");
    }

    @Test
    void testGetOrder_WhenOrderIsNull_ReturnsNull() {
        // Arrange
        orderRequest = new OrderRequest(null);

        // Act
        Order retrievedOrder = orderRequest.getOrder();

        // Assert
        assertNull(retrievedOrder, "getOrder should return null when no order was provided");
    }

    // ===== IMMUTABILITY/REFERENCE TESTS =====

    @Test
    void testGetOrder_ReturnsSameObjectReference() {
        // Arrange
        orderRequest = new OrderRequest(mockOrder);

        // Act
        Order firstCall = orderRequest.getOrder();
        Order secondCall = orderRequest.getOrder();

        // Assert
        assertSame(firstCall, secondCall, "Multiple calls to getOrder should return the same object reference");
        assertSame(mockOrder, firstCall, "getOrder should return the exact object passed to constructor");
    }

    // ===== MULTIPLE INSTANCE TESTS =====

    @Test
    void testMultipleOrderRequests_HaveSeparateOrders() {
        // Arrange
        Order order1 = mockOrder;
        Order order2 = mockOrder; // In real usage, these would be different mocks

        // Act
        OrderRequest request1 = new OrderRequest(order1);
        OrderRequest request2 = new OrderRequest(order2);

        // Assert
        assertEquals(order1, request1.getOrder(), "First request should have first order");
        assertEquals(order2, request2.getOrder(), "Second request should have second order");
    }
}
