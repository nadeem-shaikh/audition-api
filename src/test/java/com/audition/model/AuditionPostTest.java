package com.audition.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for AuditionPost model.
 * This class contains unit tests for the AuditionPost class constructors and methods.
 *
 * @author Nadeem Shaikh
 */
class AuditionPostTest {

    /**
     * Tests the no-args constructor of AuditionPost.
     */
    @Test
    void testNoArgsConstructor() {
        final AuditionPost post = new AuditionPost();
        assertNotNull(post);
    }

    /**
     * Tests the all-args constructor of AuditionPost.
     */
    @Test
    void testAllArgsConstructor() {
        final AuditionPost post = new AuditionPost(1, 2, "Title", "Body");
        assertEquals(1, post.getUserId());
        assertEquals(2, post.getId());
        assertEquals("Title", post.getTitle());
        assertEquals("Body", post.getBody());
    }

    /**
     * Tests the setters and getters of AuditionPost.
     */
    @Test
    void testSettersAndGetters() {
        final AuditionPost post = new AuditionPost();
        post.setUserId(3);
        post.setId(4);
        post.setTitle("New Title");
        post.setBody("New Body");

        assertEquals(3, post.getUserId());
        assertEquals(4, post.getId());
        assertEquals("New Title", post.getTitle());
        assertEquals("New Body", post.getBody());
    }
}