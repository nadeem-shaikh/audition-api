package com.audition.web.advice;

import com.audition.common.exception.SystemException;
import com.audition.common.logging.AuditionLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

/**
 * Test class for ExceptionControllerAdvice.
 * This class contains unit tests for the ExceptionControllerAdvice class methods.
 *
 * @author Nadeem Shaikh
 */
class ExceptionControllerAdviceTest {

    @Mock
    private transient AuditionLogger logger;

    @InjectMocks
    private transient ExceptionControllerAdvice advice;

    /**
     * Sets up the test environment before each test method.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests handling of HttpClientErrorException.
     */
    @Test
    void testHandleHttpClientException() {
        final HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request");
        final ProblemDetail result = advice.handleHttpClientException(exception);

        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
        assertEquals("400 Bad Request", result.getDetail());
        assertEquals(ExceptionControllerAdvice.DEFAULT_TITLE, result.getTitle());
    }

    /**
     * Tests handling of general exceptions.
     */
    @Test
    void testHandleMainException() {
        final Exception exception = new RuntimeException("Test exception");
        final ProblemDetail result = advice.handleMainException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getStatus());
        assertEquals("Test exception", result.getDetail());
        assertEquals(ExceptionControllerAdvice.DEFAULT_TITLE, result.getTitle());
        verify(logger).logErrorWithException(any(), any(), any());
    }

    /**
     * Tests handling of SystemException.
     */
    @Test
    void testHandleSystemException() {
        final SystemException exception = new SystemException("System error", "Custom Title", 400);
        final ProblemDetail result = advice.handleSystemException(exception);

        assertEquals(400, result.getStatus());
        assertEquals("System error", result.getDetail());
        assertEquals("Custom Title", result.getTitle());
        verify(logger).error(any(), any());
    }

    /**
     * Tests getting HTTP status code from HttpClientErrorException.
     */
    @Test
    void testGetHttpStatusCodeFromExceptionHttpClientErrorException() {
        final HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND);
        assertEquals(HttpStatus.NOT_FOUND, advice.getHttpStatusCodeFromException(exception));
    }

    /**
     * Tests getting HTTP status code from HttpRequestMethodNotSupportedException.
     */
    @Test
    void testGetHttpStatusCodeFromExceptionHttpRequestMethodNotSupportedException() {
        final HttpRequestMethodNotSupportedException exception = new HttpRequestMethodNotSupportedException("GET");
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, advice.getHttpStatusCodeFromException(exception));
    }

    /**
     * Tests getting HTTP status code from other types of exceptions.
     */
    @Test
    void testGetHttpStatusCodeFromExceptionOtherException() {
        final RuntimeException exception = new RuntimeException();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, advice.getHttpStatusCodeFromException(exception));
    }

    /**
     * Tests getting HTTP status code from SystemException with Bad Request code.
     */
    @Test
    void testGetHttpStatusCodeFromSystemExceptionBadRequestCode() {
        final SystemException exception = new SystemException("Error", 400);
        assertEquals(HttpStatus.BAD_REQUEST, advice.getHttpStatusCodeFromSystemException(exception));
    }

    /**
     * Tests getting HTTP status code from SystemException with Internal Server Error code.
     */
    @Test
    void testGetHttpStatusCodeFromSystemExceptionInternalServerErrorCode() {
        final SystemException exception = new SystemException("Error", 500);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, advice.getHttpStatusCodeFromSystemException(exception));
    }
}