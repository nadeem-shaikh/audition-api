package com.audition.service;

import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionPost;
import com.audition.model.Comment;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for handling Audition-related operations.
 *
 * @author Nadeem Shaikh
 */
@Service
public class AuditionService {

    @Autowired
    private transient AuditionIntegrationClient auditionIntegrationClient;

    /**
     * Retrieves all posts.
     *
     * @return A list of AuditionPost objects
     * @author Nadeem Shaikh
     */
    public List<AuditionPost> getPosts() {
        return auditionIntegrationClient.getPosts();
    }

    /**
     * Retrieves a specific post by its ID.
     *
     * @param postId The ID of the post to retrieve
     * @return The AuditionPost object corresponding to the given ID
     * @author Nadeem Shaikh
     */
    public AuditionPost getPostById(final String postId) {
        return auditionIntegrationClient.getPostById(postId);
    }

    /**
     * Retrieves comments for a specific post.
     *
     * @param postId The ID of the post to retrieve comments for
     * @return A list of Comment objects associated with the given post ID
     * @author Nadeem Shaikh
     */
    public List<Comment> getCommentsByPostId(final String postId) {
        return auditionIntegrationClient.getCommentsByPostId(postId);
    }

}