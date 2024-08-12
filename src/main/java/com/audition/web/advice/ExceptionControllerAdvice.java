package com.audition.web.advice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

import com.audition.common.exception.SystemException;
import com.audition.common.logging.AuditionLogger;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/**
 * Controller advice for handling exceptions across the application.
 *
 * @author Nadeem Shaikh
 */
@ControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {
    
    public static final String DEFAULT_TITLE = "API Error Occurred";
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionControllerAdvice.class);
    private static final String ERROR_MESSAGE = " Error Code from Exception could not be mapped to a valid HttpStatus Code - ";
    private static final String DEFAULT_MESSAGE = "API Error occurred. Please contact support or administrator.";

    @Autowired
    private transient AuditionLogger logger;

    /**
     * Handles HttpClientErrorException.
     *
     * @param e The HttpClientErrorException
     * @return A ProblemDetail object representing the error
     * @author Nadeem Shaikh
     */
    @ExceptionHandler(HttpClientErrorException.class)
    public ProblemDetail handleHttpClientException(final HttpClientErrorException e) {
        return createProblemDetail(e, e.getStatusCode());

    }


    /**
     * Handles general exceptions.
     *
     * @param e The Exception
     * @return A ProblemDetail object representing the error
     * @author Nadeem Shaikh
     */
    @ExceptionHandler(Exception.class)
    @SuppressWarnings("PMD.GuardLogStatement")
    public ProblemDetail handleMainException(final Exception e) {
        logger.logErrorWithException(LOG, "Unhandled exception occurred", e);
        final HttpStatusCode status = getHttpStatusCodeFromException(e);
        return createProblemDetail(e, status);

    }

    /**
     * Handles SystemException.
     *
     * @param e The SystemException
     * @return A ProblemDetail object representing the error
     * @author Nadeem Shaikh
     */
    @ExceptionHandler(SystemException.class)
    @SuppressWarnings("PMD.GuardLogStatement")
    public ProblemDetail handleSystemException(final SystemException e) {
        logger.error(LOG, "SystemException occurred: {}" + e.getMessage());
        final HttpStatusCode status = getHttpStatusCodeFromSystemException(e);
        return createProblemDetail(e, status);
    }


    /**
     * Creates a ProblemDetail object from an exception and status code.
     *
     * @param exception The Exception
     * @param statusCode The HttpStatusCode
     * @return A ProblemDetail object
     * @author Nadeem Shaikh
     */
    private ProblemDetail createProblemDetail(final Exception exception,
        final HttpStatusCode statusCode) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(statusCode);
        problemDetail.setDetail(getMessageFromException(exception));
        if (exception instanceof SystemException) {
            problemDetail.setTitle(((SystemException) exception).getTitle());
        } else {
            problemDetail.setTitle(DEFAULT_TITLE);
        }
        return problemDetail;
    }

    /**
     * Extracts a message from an exception.
     *
     * @param exception The Exception
     * @return The extracted message or a default message
     * @author Nadeem Shaikh
     */
    private String getMessageFromException(final Exception exception) {
        if (StringUtils.isNotBlank(exception.getMessage())) {
            return exception.getMessage();
        }
        return DEFAULT_MESSAGE;
    }

    /**
     * Determines the HttpStatusCode from a SystemException.
     *
     * @param exception The SystemException
     * @return The corresponding HttpStatusCode
     * @author Nadeem Shaikh
     */
    public HttpStatusCode getHttpStatusCodeFromSystemException(final SystemException exception) {
        try {
            return HttpStatusCode.valueOf(exception.getStatusCode());
        } catch (final IllegalArgumentException iae) {
            logger.info(LOG, ERROR_MESSAGE + exception.getStatusCode());
            return INTERNAL_SERVER_ERROR;
        }
    }

    /**
     * Determines the HttpStatusCode from a general Exception.
     *
     * @param exception The Exception
     * @return The corresponding HttpStatusCode
     * @author Nadeem Shaikh
     */
    public HttpStatusCode getHttpStatusCodeFromException(final Exception exception) {
        if (exception instanceof HttpClientErrorException) {
            return ((HttpClientErrorException) exception).getStatusCode();
        } else if (exception instanceof HttpRequestMethodNotSupportedException) {
            return METHOD_NOT_ALLOWED;
        }
        return INTERNAL_SERVER_ERROR;
    }
}