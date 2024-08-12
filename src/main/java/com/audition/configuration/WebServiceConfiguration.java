package com.audition.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Configuration class for web services.
 * This class sets up the necessary beans and configurations for web services,
 * including ObjectMapper and RestTemplate configurations.
 *
 * @author Nadeem Shaikh
 */
@Configuration
public class WebServiceConfiguration implements WebMvcConfigurer {

    private static final String YEAR_MONTH_DAY_PATTERN = "yyyy-MM-dd";

    /**
     * Creates and configures an ObjectMapper bean.
     *
     * @return Configured ObjectMapper instance
     * @author Nadeem Shaikh
     */
    @Bean
    public ObjectMapper objectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        
        // 1. Allow for date format as yyyy-MM-dd
        objectMapper.setDateFormat(new SimpleDateFormat(YEAR_MONTH_DAY_PATTERN, new Locale("en", "AU")));
        
        // 2. Do not fail on unknown properties
        objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        // 3. Map to camelCase
        objectMapper.setPropertyNamingStrategy(com.fasterxml.jackson.databind.PropertyNamingStrategies.LOWER_CAMEL_CASE);
        
        // 4. Do not include null values or empty values
        objectMapper.setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY);
        
        // 5. Do not write dates as timestamps
        objectMapper.configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        
        return objectMapper;
    }

    /**
     * Creates and configures a RestTemplate bean.
     *
     * @param objectMapper The ObjectMapper to be used for JSON conversion
     * @return Configured RestTemplate instance
     * @author Nadeem Shaikh
     */
    @Bean
    public RestTemplate restTemplate(final ObjectMapper objectMapper) {
        final RestTemplate restTemplate = new RestTemplate(
            new BufferingClientHttpRequestFactory(createClientFactory()));
        
        // Use object mapper
        restTemplate.setMessageConverters(List.of(new MappingJackson2HttpMessageConverter(objectMapper)));
        
        // Create a logging interceptor
        restTemplate.setInterceptors(List.of(new LoggingInterceptor()));
        
        return restTemplate;
    }

    /**
     * Creates a SimpleClientHttpRequestFactory with output streaming disabled.
     *
     * @return Configured SimpleClientHttpRequestFactory instance
     * @author Nadeem Shaikh
     */
    private SimpleClientHttpRequestFactory createClientFactory() {
        final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        return requestFactory;
    }

    /**
     * Inner class that implements ClientHttpRequestInterceptor for logging HTTP requests and responses.
     *
     * @author Nadeem Shaikh
     */
    protected class LoggingInterceptor implements ClientHttpRequestInterceptor {
        private static final Logger LOGGER = LoggerFactory.getLogger(LoggingInterceptor.class);

        /**
         * Intercepts the HTTP request, logs it, executes it, and then logs the response.
         *
         * @param request The HTTP request
         * @param body The request body
         * @param execution The request execution
         * @return The client HTTP response
         * @throws IOException If an I/O error occurs
         * @author Nadeem Shaikh
         */
        @Override
        public ClientHttpResponse intercept(final HttpRequest request, final byte[] body, final ClientHttpRequestExecution execution) throws IOException {
            logRequest(request, body);
            final   ClientHttpResponse response = execution.execute(request, body);
            logResponse(response);
            return response;
        }

        /**
         * Logs the details of an HTTP request.
         *
         * @param request The HTTP request
         * @param body The request body
         * @author Nadeem Shaikh
         */
        protected void logRequest(final HttpRequest request, final byte[] body) {
            LOGGER.info("Request: {} {}", request.getMethod(), request.getURI());
            LOGGER.info("Request headers: {}", request.getHeaders());
            LOGGER.info("Request body: {}", new String(body, StandardCharsets.UTF_8));
        }

        /**
         * Logs the details of an HTTP response.
         *
         * @param response The HTTP response
         * @throws IOException If an I/O error occurs while reading the response body
         * @author Nadeem Shaikh
         */
        protected void logResponse(final ClientHttpResponse response) throws IOException {
            LOGGER.info("Response status: {}", response.getStatusCode());
            LOGGER.info("Response headers: {}", response.getHeaders());
            LOGGER.info("Response body: {}", StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8));
        }
    }
}