package com.audition.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for Comment model.
 * This class contains unit tests for the Comment class constructors and methods.
 *
 * @author Nadeem Shaikh
 */
class CommentTest {

    /**
     * Tests the no-args constructor of Comment.
     */
    @Test
    void shouldCreateCommentWithNoArgsConstructor() {
        final Comment comment = new Comment();
        assertNotNull(comment);
    }

    /**
     * Tests the two-args constructor of Comment.
     */
    @Test
    void shouldCreateCommentWithTwoArgsConstructor() {
        final Comment comment = new Comment(1, "Test body");
        assertEquals(1, comment.getId());
        assertEquals("Test body", comment.getBody());
    }

    /**
     * Tests the setters and getters of Comment.
     */
    @Test
    void shouldSetAndGetCommentProperties() {
        final Comment comment = new Comment();
        comment.setId(1);
        comment.setPostId(2);
        comment.setName("John Doe");
        comment.setEmail("john@example.com");
        comment.setBody("Test comment body");

        assertEquals(1, comment.getId());
        assertEquals(2, comment.getPostId());
        assertEquals("John Doe", comment.getName());
        assertEquals("john@example.com", comment.getEmail());
        assertEquals("Test comment body", comment.getBody());
    }

    /**
     * Tests the toString method of Comment.
     */
    @Test
    void shouldReturnCorrectStringRepresentation() {
        final Comment comment = new Comment();
        comment.setId(1);
        comment.setPostId(2);
        comment.setName("John Doe");
        comment.setEmail("john@example.com");
        comment.setBody("Test comment body");

        final String expected = "Comment{id=1, postId=2, name='John Doe', email='john@example.com', body='Test comment body'}";
        assertEquals(expected, comment.toString());
    }
}