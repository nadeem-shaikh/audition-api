package com.audition.common.logging;

import org.slf4j.Logger;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

/**
 * Logger utility class for logging various types of messages.
 *
 * @author Nadeem Shaikh
 */
@Component
public class AuditionLogger {

    /**
     * Logs an info message with the provided logger and message.
     *
     * @param logger The logger to use for logging
     * @param message The message to log
     * @author Nadeem Shaikh
     */
    public void info(final Logger logger, final String message) {
        if (logger.isInfoEnabled()) {
            logger.info(message);
        }
    }

    /**
     * Logs an info message with the provided logger, message, and object.
     *
     * @param logger The logger to use for logging
     * @param message The message to log
     * @param object The object to log
     * @author Nadeem Shaikh
     */
    public void info(final Logger logger, final String message, final Object object) {
        if (logger.isInfoEnabled()) {
            logger.info(message, object);
        }
    }

    /**
     * Logs a debug message with the provided logger and message.
     *
     * @param logger The logger to use for logging
     * @param message The message to log
     * @author Nadeem Shaikh
     */
    public void debug(final Logger logger, final String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }

    /**
     * Logs a warning message with the provided logger and message.
     *
     * @param logger The logger to use for logging
     * @param message The message to log
     * @author Nadeem Shaikh
     */
    public void warn(final Logger logger, final String message) {
        if (logger.isWarnEnabled()) {
            logger.warn(message);
        }
    }

    /**
     * Logs an error message with the provided logger and message.
     *
     * @param logger The logger to use for logging
     * @param message The message to log
     * @author Nadeem Shaikh
     */
    public void error(final Logger logger, final String message) {
        if (logger.isErrorEnabled()) {
            logger.error(message);
        }
    }

    /**
     * Logs an error message with the provided logger, message, and exception.
     *
     * @param logger The logger to use for logging
     * @param message The message to log
     * @param e The exception to log
     * @author Nadeem Shaikh
     */
    public void logErrorWithException(final Logger logger, final String message, final Exception e) {
        if (logger.isErrorEnabled()) {
            logger.error(message, e);
        }
    }

    /**
     * Logs a ProblemDetail object with the provided logger and exception.
     *
     * @param logger The logger to use for logging
     * @param problemDetail The ProblemDetail object to log
     * @param e The exception to log
     * @author Nadeem Shaikh
     */
    public void logStandardProblemDetail(final Logger logger, final ProblemDetail problemDetail, final Exception e) {
        if (logger.isErrorEnabled()) {
            final var message = createStandardProblemDetailMessage(problemDetail);
            logger.error(message, e);
        }
    }

    /**
     * Logs an HTTP status code error with the provided logger, message, and error code.
     *
     * @param logger The logger to use for logging
     * @param message The message to log
     * @param errorCode The error code to log
     * @author Nadeem Shaikh
     */
    public void logHttpStatusCodeError(final Logger logger, final String message, final Integer errorCode) {
        if (logger.isErrorEnabled()) {
            logger.error(createBasicErrorResponseMessage(errorCode, message) + "\n");
        }
    }

    /**
     * Creates a formatted message string from a ProblemDetail object.
     *
     * @param standardProblemDetail The ProblemDetail object to format
     * @return A formatted string containing problem details
     * @author Nadeem Shaikh
     */
    private String createStandardProblemDetailMessage(final ProblemDetail standardProblemDetail) {
        return String.format("Problem: %s | Status: %d | Detail: %s | Type: %s",
            standardProblemDetail.getTitle(),
            standardProblemDetail.getStatus(),
            standardProblemDetail.getDetail(),
            standardProblemDetail.getType());
    }

    /**
     * Creates a basic error response message from an error code and message.
     *
     * @param errorCode The error code
     * @param message The error message
     * @return A formatted string containing the error details
     * @author Nadeem Shaikh
     */
    private String createBasicErrorResponseMessage(final Integer errorCode, final String message) {
        return String.format("Error: %s | Code: %d", message, errorCode);
    }
}