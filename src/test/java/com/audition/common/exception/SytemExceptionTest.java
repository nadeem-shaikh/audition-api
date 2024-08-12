package com.audition.common.exception;

import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for SystemException.
 * This class contains unit tests for various constructors and methods of the SystemException class.
 * 
 * @author Nadeem Shaikh
 */
class SytemExceptionTest {

    private static final String TEST_MESSAGE = "Test message";
    private static final String TEST_DETAIL = "Test detail";
    private static final String TEST_TITLE = "Test title";
    private static final String CAUSE = "Cause";

    /**
     * Tests the default constructor of SystemException.
     * Verifies that all fields are null when using the no-args constructor.
     */
    @Test
    void testDefaultConstructor() {
        final SystemException ex = new SystemException();
        assertNull(ex.getMessage());
        assertNull(ex.getStatusCode());
        assertNull(ex.getTitle());
        assertNull(ex.getDetail());
    }

    /**
     * Tests the constructor that takes only a message.
     * Verifies that the message is set correctly and other fields have expected values.
     */
    @Test
    void testMessageConstructor() {
        final SystemException ex = new SystemException(TEST_MESSAGE);
        assertEquals(TEST_MESSAGE, ex.getMessage());
        assertEquals(SystemException.DEFAULT_TITLE, ex.getTitle());
        assertNull(ex.getStatusCode());
        assertNull(ex.getDetail());
    }

    /**
     * Tests the constructor that takes a message and an error code.
     * Verifies that both message and status code are set correctly.
     */
    @Test
    void testMessageAndErrorCodeConstructor() {
        final SystemException ex = new SystemException(TEST_MESSAGE, 400);
        assertEquals(TEST_MESSAGE, ex.getMessage());
        assertEquals(SystemException.DEFAULT_TITLE, ex.getTitle());
        assertEquals(400, ex.getStatusCode());
        assertNull(ex.getDetail());
    }

    /**
     * Tests the constructor that takes detail, title, and error code.
     * Verifies that all three parameters are set correctly in the exception.
     */
    @Test
    void testDetailTitleErrorCodeConstructor() {
        final SystemException ex = new SystemException(TEST_DETAIL, TEST_TITLE, 404);
        assertEquals(TEST_DETAIL, ex.getMessage());
        assertEquals(TEST_TITLE, ex.getTitle());
        assertEquals(404, ex.getStatusCode());
        assertEquals(TEST_DETAIL, ex.getDetail());
    }

    /**
     * Tests the constructor that takes detail, title, and a cause exception.
     * Verifies that these parameters are set correctly and the status code defaults to 500.
     */
    @Test
    void testDetailTitleExceptionConstructor() {
        final Exception cause = new Exception(CAUSE);
        final SystemException ex = new SystemException(TEST_DETAIL, TEST_TITLE, cause);
        assertEquals(TEST_DETAIL, ex.getMessage());
        assertEquals(TEST_TITLE, ex.getTitle());
        assertEquals(500, ex.getStatusCode());
        assertEquals(TEST_DETAIL, ex.getDetail());
        assertEquals(cause, ex.getCause());
    }

    /**
     * Tests the constructor that takes detail, error code, and a cause exception.
     * Verifies that these parameters are set correctly and the title uses the default value.
     */
    @Test
    void testDetailErrorCodeExceptionConstructor() {
        final Exception cause = new Exception(CAUSE);
        final SystemException ex = new SystemException(TEST_DETAIL, 403, cause);
        assertEquals(TEST_DETAIL, ex.getMessage());
        assertEquals(SystemException.DEFAULT_TITLE, ex.getTitle());
        assertEquals(403, ex.getStatusCode());
        assertEquals(TEST_DETAIL, ex.getDetail());
        assertEquals(cause, ex.getCause());
    }

    /**
     * Tests the constructor that takes all parameters: detail, title, error code, and cause.
     * Verifies that all parameters are set correctly in the exception.
     */
    @Test
    void testAllArgsConstructor() {
        final Exception cause = new Exception(CAUSE);
        final SystemException ex = new SystemException(TEST_DETAIL, TEST_TITLE, 401, cause);
        assertEquals(TEST_DETAIL, ex.getMessage());
        assertEquals(TEST_TITLE, ex.getTitle());
        assertEquals(401, ex.getStatusCode());
        assertEquals(TEST_DETAIL, ex.getDetail());
        assertEquals(cause, ex.getCause());
    }
}