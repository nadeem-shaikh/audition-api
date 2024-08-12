package com.audition.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.when;
import com.audition.model.AuditionPost;
import com.audition.model.Comment;
import com.audition.service.AuditionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test class for AuditionController.
 * This class contains unit tests for the AuditionController class methods.
 *
 * @author Nadeem Shaikh
 */
class AuditionControllerTest {

    private static final String POST_1_TITLE = "Title of post 1";
    private static final String POST_2_TITLE = "Title of post 2";
    private static final String POST_1_BODY = "Body of post 1";
    private static final String POST_2_BODY = "Body of post 2";

    @Mock
    private transient AuditionService auditionService;

    @InjectMocks
    private transient AuditionController auditionController;

    private transient MockMvc mockMvc;

    /**
     * Sets up the test environment before each test method.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(auditionController).build();
    }

    /**
     * Tests the getPosts method of AuditionController.
     */
    @Test
    void testGetPosts() throws Exception {
        final List<AuditionPost> posts = Arrays.asList(
            new AuditionPost(1, 1, POST_1_TITLE, POST_1_BODY),
            new AuditionPost(1, 2, POST_2_TITLE, POST_2_BODY)
        );
        when(auditionService.getPosts()).thenReturn(posts);

        mockMvc.perform(get("/posts").param("userId", "1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[1].id").value(2));

        assertEquals(2, posts.size(), "Should return 2 posts");
        assertEquals(POST_1_TITLE, posts.get(0).getTitle(), "First post title should match");
        assertEquals(POST_2_TITLE, posts.get(1).getTitle(), "Second post title should match");
        assertEquals(POST_1_BODY, posts.get(0).getBody(), "First post body should match");
        assertEquals(POST_2_BODY, posts.get(1).getBody(), "Second post body should match");
    }

    /**
     * Tests the getPostById method of AuditionController.
     */
    @Test
    void testGetPostById() throws Exception {
        final AuditionPost post = new AuditionPost(1, 1, POST_1_TITLE, POST_1_BODY);
        when(auditionService.getPostById("1")).thenReturn(post);

        mockMvc.perform(get("/posts/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").value(POST_1_TITLE));

        assertNotNull(post, "Post should not be null");
        assertEquals(1, post.getId(), "Post ID should be 1");
        assertEquals(POST_1_TITLE, post.getTitle(), "Post title should match");
        assertEquals(POST_1_BODY, post.getBody(), "Post body should match");
    }

    /**
     * Tests the getCommentsByPostId method of AuditionController.
     */
    @Test
    void testGetCommentsByPostId() throws Exception {
        final List<Comment> comments = Arrays.asList(
            new Comment(1, "Comment 1"),
            new Comment(2, "Comment 2")
        );
        when(auditionService.getCommentsByPostId("1")).thenReturn(comments);

        mockMvc.perform(get("/posts/1/comments"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[1].id").value(2));

        assertEquals(2, comments.size(), "Should return 2 comments");
        assertEquals("Comment 1", comments.get(0).getBody(), "First comment body should match");
        assertEquals("Comment 2", comments.get(1).getBody(), "Second comment body should match");
    }

    /**
     * Tests that getPosts throws a BadRequest exception when given a blank ID.
     */
    @Test
    void testGetPostsByIdBlankIdThrowsBadRequest() {
        assertThrows(ResponseStatusException.class, () -> auditionController.getPosts(""));
    }

    /**
     * Tests that getPosts throws a NotFound exception when the post is null.
     */
    @Test
    void testGetPostsByIdNullPostThrowsNotFound() {
        when(auditionService.getPostById("1")).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> auditionController.getPosts("1"));
    }

    /**
     * Tests that getCommentsByPostId throws a BadRequest exception when given a blank ID.
     */
    @Test
    void testGetCommentsByPostIdBlankIdThrowsBadRequest() {
        assertThrows(ResponseStatusException.class, () -> auditionController.getCommentsByPostId(""));
    }
}