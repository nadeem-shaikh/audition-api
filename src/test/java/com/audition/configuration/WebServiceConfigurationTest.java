package com.audition.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpHeaders;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Test class for WebServiceConfiguration.
 * This class contains unit tests for various components and methods of the WebServiceConfiguration class.
 *
 * @author Nadeem Shaikh
 */
class WebServiceConfigurationTest {

    private transient WebServiceConfiguration configuration;
    private transient WebServiceConfiguration.LoggingInterceptor loggingInterceptor;

    @Mock
    private transient HttpRequest mockRequest;
    @Mock
    private transient ClientHttpResponse mockResponse;

    /**
     * Sets up the test environment before each test method.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        configuration = new WebServiceConfiguration();
        loggingInterceptor = configuration.new LoggingInterceptor();
    }

    /**
     * Tests the configuration of the ObjectMapper.
     */
    @Test
    void testObjectMapper() {
        final ObjectMapper objectMapper = configuration.objectMapper();

        // Test date format
        final SimpleDateFormat dateFormat = (SimpleDateFormat) objectMapper.getDateFormat();
        assertEquals("yyyy-MM-dd", dateFormat.toPattern());

        // Test FAIL_ON_UNKNOWN_PROPERTIES
        assertFalse(objectMapper.getDeserializationConfig().isEnabled(
            com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES));

        // Test property naming strategy
        assertEquals(com.fasterxml.jackson.databind.PropertyNamingStrategies.LOWER_CAMEL_CASE,
            objectMapper.getPropertyNamingStrategy());

        // Test serialization inclusion
        assertEquals(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY,
            objectMapper.getSerializationConfig().getDefaultPropertyInclusion().getValueInclusion());

        // Test WRITE_DATES_AS_TIMESTAMPS
        assertFalse(objectMapper.getSerializationConfig().isEnabled(
            com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
    }

    /**
     * Tests the configuration of the RestTemplate.
     */
    @Test
    void testRestTemplate() {
        final ObjectMapper objectMapper = configuration.objectMapper();
        final RestTemplate restTemplate = configuration.restTemplate(objectMapper);

        // Test message converters
        assertEquals(1, restTemplate.getMessageConverters().size());
        assertTrue(restTemplate.getMessageConverters().get(0) instanceof MappingJackson2HttpMessageConverter);

        // Test interceptors
        assertEquals(1, restTemplate.getInterceptors().size());
        assertTrue(restTemplate.getInterceptors().get(0) instanceof WebServiceConfiguration.LoggingInterceptor);
    }

    /**
     * Tests the date serialization of the ObjectMapper.
     */
    @Test
    void testObjectMapperDateSerialization() throws Exception {
        final ObjectMapper objectMapper = configuration.objectMapper();
        final Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse("2023-04-15");
        final String json = objectMapper.writeValueAsString(date);
        assertEquals("\"2023-04-15\"", json);
    }

    /**
     * Tests the handling of unknown properties by the ObjectMapper.
     */
    @Test
    void testObjectMapperUnknownProperties() throws Exception {
        final ObjectMapper objectMapper = configuration.objectMapper();
        final String json = "{\"knownField\": \"value\", \"unknownField\": \"ignored\"}";
        final TestClass result = objectMapper.readValue(json, TestClass.class);
        
        assertEquals("value", result.knownField);
        assertDoesNotThrow(() -> {
            objectMapper.readValue(json, TestClass.class);
        });
    }

    /**
     * Tests the logging of HTTP requests.
     */
    @Test
    void testLogRequest() throws IOException {
        when(mockRequest.getMethod()).thenReturn(HttpMethod.GET);
        when(mockRequest.getURI()).thenReturn(URI.create("http://example.com"));
        when(mockRequest.getHeaders()).thenReturn(new HttpHeaders());

        final byte[] body = "Test body".getBytes(StandardCharsets.UTF_8);
        assertDoesNotThrow(() -> {
            loggingInterceptor.logRequest(mockRequest, body);
        });
    }

    /**
     * Tests the logging of HTTP responses.
     */
    @Test
    void testLogResponse() throws IOException {
        when(mockResponse.getStatusCode()).thenReturn(HttpStatus.OK);
        when(mockResponse.getHeaders()).thenReturn(new HttpHeaders());
        when(mockResponse.getBody()).thenReturn(new ByteArrayInputStream("Test response".getBytes(StandardCharsets.UTF_8)));

        assertDoesNotThrow(() -> loggingInterceptor.logResponse(mockResponse));
    }

    @SuppressWarnings("PMD.BeanMembersShouldSerialize")
    private static class TestClass {
        public String knownField;
    }
}