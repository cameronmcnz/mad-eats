package com.eatrading.api.objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for OrderResponse class.
 * 
 * These tests focus on OrderResponse's behavior:
 * - Default construction initializes status to SUBMITTED and rejection reason to empty string
 * - Getting and setting status codes
 * - Getting and setting rejection reasons
 * - Handling all Status enum values
 */
@ExtendWith(MockitoExtension.class)
class OrderResponseTest {

    private OrderResponse orderResponse;

    @BeforeEach
    void setUp() {
        orderResponse = new OrderResponse();
    }

    // ===== DEFAULT CONSTRUCTOR TESTS =====

    @Test
    void testDefaultConstructor_InitializesStatusToSubmitted() {
        // Act - orderResponse is created in setUp
        Status status = orderResponse.getStatusCode();

        // Assert
        assertNotNull(status, "Status should not be null after default construction");
        assertEquals(Status.SUBMITTED, status, "Default constructor should initialize status to SUBMITTED");
    }

    @Test
    void testDefaultConstructor_InitializesRejectionReasonToEmptyString() {
        // Act - orderResponse is created in setUp
        String rejectionReason = orderResponse.getRejectionReason();

        // Assert
        assertNotNull(rejectionReason, "Rejection reason should not be null after default construction");
        assertEquals("", rejectionReason, "Default constructor should initialize rejection reason to empty string");
    }

    @Test
    void testDefaultConstructor_CreatesValidInstance() {
        // Act
        OrderResponse newResponse = new OrderResponse();

        // Assert
        assertNotNull(newResponse, "OrderResponse instance should be created successfully");
        assertNotNull(newResponse.getStatusCode(), "Status code should not be null");
        assertNotNull(newResponse.getRejectionReason(), "Rejection reason should not be null");
    }

    // ===== STATUS CODE GETTER/SETTER TESTS =====

    @Test
    void testSetStatusCode_WithSubmitted_UpdatesStatus() {
        // Arrange
        Status newStatus = Status.SUBMITTED;

        // Act
        orderResponse.setStatusCode(newStatus);
        Status retrievedStatus = orderResponse.getStatusCode();

        // Assert
        assertEquals(newStatus, retrievedStatus, "setStatusCode should update the status code");
    }

    @Test
    void testSetStatusCode_WithRejected_UpdatesStatus() {
        // Arrange
        Status newStatus = Status.REJECTED;

        // Act
        orderResponse.setStatusCode(newStatus);

        // Assert
        assertEquals(Status.REJECTED, orderResponse.getStatusCode(), "Status should be updated to REJECTED");
    }

    @Test
    void testSetStatusCode_WithFilled_UpdatesStatus() {
        // Arrange
        Status newStatus = Status.FILLED;

        // Act
        orderResponse.setStatusCode(newStatus);

        // Assert
        assertEquals(Status.FILLED, orderResponse.getStatusCode(), "Status should be updated to FILLED");
    }

    @Test
    void testSetStatusCode_WithAccepted_UpdatesStatus() {
        // Arrange
        Status newStatus = Status.ACCEPTED;

        // Act
        orderResponse.setStatusCode(newStatus);

        // Assert
        assertEquals(Status.ACCEPTED, orderResponse.getStatusCode(), "Status should be updated to ACCEPTED");
    }

    @ParameterizedTest
    @EnumSource(Status.class)
    void testSetStatusCode_WithAllStatusValues_UpdatesCorrectly(Status status) {
        // Act
        orderResponse.setStatusCode(status);

        // Assert
        assertEquals(status, orderResponse.getStatusCode(), 
                     "Status should be correctly updated to " + status);
    }

    @Test
    void testGetStatusCode_ReturnsPreviouslySetValue() {
        // Arrange
        Status firstStatus = Status.SUBMITTED;
        Status secondStatus = Status.REJECTED;

        // Act
        orderResponse.setStatusCode(firstStatus);
        Status firstRetrieved = orderResponse.getStatusCode();
        
        orderResponse.setStatusCode(secondStatus);
        Status secondRetrieved = orderResponse.getStatusCode();

        // Assert
        assertEquals(firstStatus, firstRetrieved, "First get should return first set value");
        assertEquals(secondStatus, secondRetrieved, "Second get should return second set value");
    }

    // ===== REJECTION REASON GETTER/SETTER TESTS =====

    @Test
    void testSetRejectionReason_WithValidReason_UpdatesReason() {
        // Arrange
        String reason = "Insufficient funds";

        // Act
        orderResponse.setRejectionReason(reason);
        String retrievedReason = orderResponse.getRejectionReason();

        // Assert
        assertEquals(reason, retrievedReason, "setRejectionReason should update the rejection reason");
    }

    @Test
    void testSetRejectionReason_WithEmptyString_UpdatesToEmpty() {
        // Arrange
        orderResponse.setRejectionReason("Some reason");
        String emptyReason = "";

        // Act
        orderResponse.setRejectionReason(emptyReason);

        // Assert
        assertEquals("", orderResponse.getRejectionReason(), 
                     "setRejectionReason should allow empty string");
    }

    @Test
    void testSetRejectionReason_WithLongReason_StoresFullString() {
        // Arrange
        String longReason = "Order rejected due to market conditions: insufficient liquidity and price volatility";

        // Act
        orderResponse.setRejectionReason(longReason);

        // Assert
        assertEquals(longReason, orderResponse.getRejectionReason(), 
                     "setRejectionReason should store the complete long string");
    }

    @Test
    void testSetRejectionReason_WithSpecialCharacters_PreservesContent() {
        // Arrange
        String specialReason = "Order rejected: Client@email.com, Amount=$1000, Risk: 25%";

        // Act
        orderResponse.setRejectionReason(specialReason);

        // Assert
        assertEquals(specialReason, orderResponse.getRejectionReason(), 
                     "setRejectionReason should preserve special characters");
    }

    @Test
    void testSetRejectionReason_MultipleUpdates_StoresLatestValue() {
        // Arrange
        String firstReason = "Initial reason";
        String secondReason = "Updated reason";
        String thirdReason = "Final reason";

        // Act
        orderResponse.setRejectionReason(firstReason);
        orderResponse.setRejectionReason(secondReason);
        orderResponse.setRejectionReason(thirdReason);

        // Assert
        assertEquals(thirdReason, orderResponse.getRejectionReason(), 
                     "Should store the most recent rejection reason");
    }

    @Test
    void testGetRejectionReason_WithNullAfterSet_ReturnsStoredValue() {
        // Arrange
        String reason = "Market closed";

        // Act
        orderResponse.setRejectionReason(reason);
        String retrieved = orderResponse.getRejectionReason();

        // Assert
        assertNotNull(retrieved, "getRejectionReason should never return null after being set");
        assertEquals(reason, retrieved, "getRejectionReason should return the set value");
    }

    // ===== COMBINED STATE TESTS =====

    @Test
    void testStatusAndRejectionReason_BothUpdateIndependently() {
        // Arrange
        Status newStatus = Status.REJECTED;
        String rejectionReason = "Invalid order parameters";

        // Act
        orderResponse.setStatusCode(newStatus);
        orderResponse.setRejectionReason(rejectionReason);

        // Assert
        assertEquals(newStatus, orderResponse.getStatusCode(), 
                     "Status should be updated independently");
        assertEquals(rejectionReason, orderResponse.getRejectionReason(), 
                     "Rejection reason should be updated independently");
    }

    @Test
    void testCompleteWorkflow_SetBothPropertiesMultipleTimes() {
        // Arrange - Act - Assert
        // First state: Submitted with no rejection reason
        assertEquals(Status.SUBMITTED, orderResponse.getStatusCode());
        assertEquals("", orderResponse.getRejectionReason());

        // Change to accepted
        orderResponse.setStatusCode(Status.ACCEPTED);
        assertEquals(Status.ACCEPTED, orderResponse.getStatusCode());
        assertEquals("", orderResponse.getRejectionReason());

        // Change to rejected with reason
        orderResponse.setStatusCode(Status.REJECTED);
        orderResponse.setRejectionReason("Price limit exceeded");
        assertEquals(Status.REJECTED, orderResponse.getStatusCode());
        assertEquals("Price limit exceeded", orderResponse.getRejectionReason());

        // Change to filled
        orderResponse.setStatusCode(Status.FILLED);
        orderResponse.setRejectionReason("");
        assertEquals(Status.FILLED, orderResponse.getStatusCode());
        assertEquals("", orderResponse.getRejectionReason());
    }

    // ===== MULTIPLE INSTANCE TESTS =====

    @Test
    void testMultipleOrderResponses_HaveIndependentStates() {
        // Act
        OrderResponse response1 = new OrderResponse();
        OrderResponse response2 = new OrderResponse();

        response1.setStatusCode(Status.REJECTED);
        response1.setRejectionReason("Reason 1");

        response2.setStatusCode(Status.FILLED);
        response2.setRejectionReason("Reason 2");

        // Assert
        assertEquals(Status.REJECTED, response1.getStatusCode());
        assertEquals("Reason 1", response1.getRejectionReason());
        
        assertEquals(Status.FILLED, response2.getStatusCode());
        assertEquals("Reason 2", response2.getRejectionReason());
    }

    // ===== EDGE CASES =====

    @Test
    void testSetRejectionReason_NullIsAllowed_CurrentBehavior() {
        OrderResponse r = new OrderResponse();
        r.setRejectionReason(null);
        // Current implementation has a String field with no guard; test current behavior
        assertNull(r.getRejectionReason());
        // TODO: If null should be disallowed, update implementation and change test to expect empty string or NPE.
    }

    @Test
    void testSetStatusCode_NullIsAllowed_CurrentBehavior() {
        OrderResponse r = new OrderResponse();
        r.setStatusCode(null);
        assertNull(r.getStatusCode());
        // TODO: Decide if statusCode should never be null; add validation if needed.
    }
}
