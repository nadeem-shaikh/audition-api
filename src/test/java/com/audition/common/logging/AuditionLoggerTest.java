package com.audition.common.logging;

import org.slf4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ProblemDetail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;

import org.mockito.Mockito;

/**
 * Test class for AuditionLogger.
 * This class contains unit tests for various logging methods of the AuditionLogger class.
 *
 * @author Nadeem Shaikh
 */
@SuppressWarnings("PMD.MoreThanOneLogger")
class AuditionLoggerTest {

    private static final Logger LOGGER = Mockito.mock(Logger.class);
    //  private static final Logger LOGGER = LoggerFactory.getLogger(AuditionLoggerTest.class);


    private transient AuditionLogger auditionLogger;

    /**
     * Sets up the test environment before each test method.
     */
    @BeforeEach
    void setUp() {
        auditionLogger = new AuditionLogger();
    }

    /**
     * Tests the info logging method with a simple message.
     */
    @Test
    void testInfo() {       
        when(LOGGER.isInfoEnabled()).thenReturn(true);
        auditionLogger.info(LOGGER, "Test message");
        verify(LOGGER).info(eq("Test message"));
    }
    
    /**
     * Tests the info logging method with a message and an object parameter.
     */
    @Test
    void testInfoWithObject() {
        when(LOGGER.isInfoEnabled()).thenReturn(true);
        final Object testObject = new Object();
        auditionLogger.info(LOGGER, "Test message {}", testObject);
        verify(LOGGER).info("Test message {}", testObject);
    }

    /**
     * Tests the debug logging method.
     */
    @Test
    void testDebug() {
        when(LOGGER.isDebugEnabled()).thenReturn(true);
        auditionLogger.debug(LOGGER, "Debug message");
        verify(LOGGER).debug("Debug message");
    }

    /**
     * Tests the warn logging method.
     */
    @Test
    void testWarn() {
        when(LOGGER.isWarnEnabled()).thenReturn(true);
        auditionLogger.warn(LOGGER, "Warning message");
        verify(LOGGER).warn("Warning message");
    }

    /**
     * Tests the error logging method.
     */
    @Test
    void testError() {
        when(LOGGER.isErrorEnabled()).thenReturn(true);
        auditionLogger.error(LOGGER, "Error message");
        verify(LOGGER).error("Error message");
    }

    /**
     * Tests logging an error message with an associated exception.
     */
    @Test
    void testLogErrorWithException() {
        when(LOGGER.isErrorEnabled()).thenReturn(true);
        final Exception testException = new Exception("Test exception");
        auditionLogger.logErrorWithException(LOGGER, "Error with exception", testException);
        verify(LOGGER).error("Error with exception", testException);
    }

    /**
     * Tests logging a standard problem detail with an associated exception.
     */
    @Test
    void testLogStandardProblemDetail() {
        when(LOGGER.isErrorEnabled()).thenReturn(true);
        final ProblemDetail problemDetail = ProblemDetail.forStatus(404);
        problemDetail.setTitle("Not Found");
        problemDetail.setDetail("Resource not found");
        final Exception testException = new Exception("Test exception");
        auditionLogger.logStandardProblemDetail(LOGGER, problemDetail, testException);
        verify(LOGGER).error(contains("Problem: Not Found | Status: 404 | Detail: Resource not found"), eq(testException));
    }

    /**
     * Tests logging an HTTP status code error.
     */
    @Test
    void testLogHttpStatusCodeError() {
        when(LOGGER.isErrorEnabled()).thenReturn(true);
        auditionLogger.logHttpStatusCodeError(LOGGER, "Bad Request", 400);
        verify(LOGGER).error(contains("Error: Bad Request | Code: 400"));
    }
}