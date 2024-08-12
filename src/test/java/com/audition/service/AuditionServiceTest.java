package com.audition.service;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionPost;
import com.audition.model.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for AuditionService.
 * This class contains unit tests for the AuditionService class methods.
 *
 * @author Nadeem Shaikh
 */
class AuditionServiceTest {

    private static final String POST_1_TITLE = "Title of post 1";
    private static final String POST_2_TITLE = "Title of post 2";
    private static final String POST_1_BODY = "Body of post 1";
    private static final String POST_2_BODY = "Body of post 2";

    @Mock
    private transient AuditionIntegrationClient auditionIntegrationClient;

    @InjectMocks
    private transient AuditionService auditionService;

    /**
     * Sets up the test environment before each test method.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the getPosts method of AuditionService.
     */
    @Test
    void testGetPosts() {
        final List<AuditionPost> posts = Arrays.asList(
            new AuditionPost(1, 1, POST_1_TITLE, POST_1_BODY),
            new AuditionPost(2, 1, POST_2_TITLE, POST_2_BODY)
        );
        when(auditionIntegrationClient.getPosts()).thenReturn(posts);
        final List<AuditionPost> result = auditionService.getPosts();
        assertEquals(2, result.size());
        assertEquals(POST_1_TITLE, result.get(0).getTitle());
        assertEquals(POST_1_BODY, result.get(0).getBody());
    }

    /**
     * Tests the getPostById method of AuditionService.
     */
    @Test
    void testGetPostById() {
        final AuditionPost post = new AuditionPost(1, 2, POST_1_TITLE, POST_1_BODY);
        when(auditionIntegrationClient.getPostById("1")).thenReturn(post);
        final AuditionPost result = auditionService.getPostById("1");
        assertNotNull(result);
        assertEquals(POST_1_TITLE, result.getTitle());
        assertEquals(POST_1_BODY, result.getBody());
    }

    /**
     * Tests the getCommentsByPostId method of AuditionService.
     */
    @Test
    void testGetCommentsByPostId() {
        final List<Comment> comments = Arrays.asList(new Comment(1, "Comment 1"), new Comment(2, "Comment 2"));
        when(auditionIntegrationClient.getCommentsByPostId("1")).thenReturn(comments);
        final List<Comment> result = auditionService.getCommentsByPostId("1");
        assertEquals(2, result.size());
        assertEquals("Comment 1", result.get(0).getBody());
    }
}