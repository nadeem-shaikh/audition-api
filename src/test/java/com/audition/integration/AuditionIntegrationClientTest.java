package com.audition.integration;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import com.audition.model.AuditionPost;
import com.audition.model.Comment;
import com.audition.common.exception.SystemException;

/**
 * Test class for AuditionIntegrationClient.
 * This class contains unit tests for various methods of the AuditionIntegrationClient class.
 * 
 * @author Nadeem Shaikh
 */
@SuppressWarnings("PMD.TooManyMethods")
class AuditionIntegrationClientTest {

    @Mock
    private transient RestTemplate restTemplate;

    @InjectMocks
    private transient AuditionIntegrationClient auditionIntegrationClient;

    private static final String API_ERROR = "API Error";
    private static final String RESOURCE_NOT_FOUND = "Resource Not Found";
    private static final String TEST_POST_ID = "1";

    /**
     * Sets up the test environment before each test method.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the getPosts method for successful retrieval of posts.
     */
    @Test
    void testGetPosts() {
        // Arrange
        final List<AuditionPost> mockPosts = Collections.singletonList(new AuditionPost());
        final ResponseEntity<List<AuditionPost>> responseEntity = ResponseEntity.ok(mockPosts);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(),
            any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

        // Act  
        final List<AuditionPost> posts = auditionIntegrationClient.getPosts();

        // Assert
        assertNotNull(posts);
        assertEquals(1, posts.size());
    }

    /**
     * Tests the getPostById method for successful retrieval of a post by ID.
     */
    @Test
    void testGetPostById() {
        // Arrange
        final String postId = TEST_POST_ID;
        final AuditionPost mockPost = new AuditionPost();
        final ResponseEntity<AuditionPost> responseEntity = ResponseEntity.ok(mockPost);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), 
            eq(AuditionPost.class), eq(postId))).thenReturn(responseEntity);

        // Act
        final AuditionPost post = auditionIntegrationClient.getPostById(postId);

        // Assert
        assertNotNull(post);
    }

    /**
     * Tests the getCommentsByPostId method for successful retrieval of comments for a post.
     */
    @Test
    void testGetCommentsByPostId() {
        // Arrange
        final String postId = TEST_POST_ID;
        final List<Comment> mockComments = Collections.singletonList(new Comment());
        final ResponseEntity<List<Comment>> responseEntity = ResponseEntity.ok(mockComments);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), 
            any(ParameterizedTypeReference.class), eq(postId)))
            .thenReturn(responseEntity);

        // Act
        final List<Comment> comments = auditionIntegrationClient.getCommentsByPostId(postId);

        // Assert
        assertNotNull(comments);
        assertEquals(1, comments.size());
    }

    /**
     * Tests the getCommentsForPost method for successful retrieval of comments for a post.
     */
    @Test
    void testGetCommentsForPost() {
        // Arrange
        final String postId = TEST_POST_ID;
        final List<Comment> mockComments = Collections.singletonList(new Comment());
        final ResponseEntity<List<Comment>> responseEntity = ResponseEntity.ok(mockComments);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), 
            any(ParameterizedTypeReference.class), eq(postId)))
            .thenReturn(responseEntity);

        // Act
        final List<Comment> comments = auditionIntegrationClient.getCommentsForPost(postId);

        // Assert
        assertNotNull(comments);
        assertEquals(1, comments.size());
    }

    /**
     * Tests that getPosts throws a SystemException when an HttpClientErrorException occurs.
     */
    @Test
    void testGetPostsThrowsSystemExceptionOnHttpClientErrorException() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        final SystemException exception = assertThrows(SystemException.class, () -> auditionIntegrationClient.getPosts());
        assertEquals(API_ERROR, exception.getTitle());
        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
    }

    /**
     * Tests that getPostById throws a SystemException when a resource is not found.
     */
    @Test
    void testGetPostByIdThrowsSystemExceptionOnNotFound() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), eq(AuditionPost.class), anyString()))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        final SystemException exception = assertThrows(SystemException.class, () -> auditionIntegrationClient.getPostById(TEST_POST_ID));
        assertEquals(RESOURCE_NOT_FOUND, exception.getTitle());
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
    }

    /**
     * Tests that getPostById throws a SystemException for non-404 HttpClientErrorExceptions.
     */
    @Test
    void testGetPostByIdThrowsSystemExceptionOnOtherHttpClientErrorException() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), eq(AuditionPost.class), anyString()))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        final SystemException exception = assertThrows(SystemException.class, () -> auditionIntegrationClient.getPostById(TEST_POST_ID));
        assertEquals(API_ERROR, exception.getTitle());
        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
    }

    /**
     * Tests that getCommentsByPostId throws a SystemException when a resource is not found.
     */
    @Test
    void testGetCommentsByPostIdThrowsSystemExceptionOnNotFound() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class), anyString()))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        final SystemException exception = assertThrows(SystemException.class, () -> auditionIntegrationClient.getCommentsByPostId(TEST_POST_ID));
        assertEquals(RESOURCE_NOT_FOUND, exception.getTitle());
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
    }

    /**
     * Tests that getCommentsByPostId throws a SystemException for non-404 HttpClientErrorExceptions.
     */
    @Test
    void testGetCommentsByPostIdThrowsSystemExceptionOnOtherHttpClientErrorException() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class), anyString()))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        final SystemException exception = assertThrows(SystemException.class, () -> auditionIntegrationClient.getCommentsByPostId(TEST_POST_ID));
        assertEquals(API_ERROR, exception.getTitle());
        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
    }

    /**
     * Tests that getCommentsForPost throws a SystemException when a resource is not found.
     */
    @Test
    void testGetCommentsForPostThrowsSystemExceptionOnNotFound() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class), anyString()))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        final SystemException exception = assertThrows(SystemException.class, () -> auditionIntegrationClient.getCommentsForPost(TEST_POST_ID));
        assertEquals(RESOURCE_NOT_FOUND, exception.getTitle());
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
    }

    /**
     * Tests that getCommentsForPost throws a SystemException for non-404 HttpClientErrorExceptions.
     */
    @Test
    void testGetCommentsForPostThrowsSystemExceptionOnOtherHttpClientErrorException() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class), anyString()))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        final SystemException exception = assertThrows(SystemException.class, () -> auditionIntegrationClient.getCommentsForPost(TEST_POST_ID));
        assertEquals(API_ERROR, exception.getTitle());
        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
    }
}