package com.audition.integration;

import com.audition.common.exception.SystemException;
import com.audition.model.AuditionPost;
import com.audition.model.Comment;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Client for integrating with the Audition external API.
 *
 * @author Nadeem Shaikh
 */
@Component
public class AuditionIntegrationClient {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private static final String RESOURCE_NOT_FOUND = "Resource Not Found";
    private static final String API_ERROR = "API Error";
    private static final String ERROR_FETCHING_POST = "Error fetching post: ";
    private static final String ERROR_FETCHING_COMMENTS = "Error fetching comments for post: ";
    private static final String CANNOT_FIND_POST = "Cannot find a Post with id ";
    private static final String CANNOT_FIND_COMMENTS = "Cannot find comments for post with id ";

    @Autowired
    private transient RestTemplate restTemplate;

    /**
     * Retrieves all posts from the external API.
     *
     * @return A list of AuditionPost objects
     * @throws SystemException if there's an error fetching posts
     * @author Nadeem Shaikh
     */
    public List<AuditionPost> getPosts() {
        try {
            final ResponseEntity<List<AuditionPost>> response = restTemplate.exchange(
                BASE_URL + "/posts",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
            );
            final List<AuditionPost> posts = response.getBody();
            return posts != null ? posts : new ArrayList<>();

        } catch (final HttpClientErrorException e) {

            throw new SystemException(ERROR_FETCHING_POST + e.getMessage(), API_ERROR, e.getStatusCode().value(), e);
        }
    }

    /**
     * Retrieves a specific post by its ID from the external API.
     *
     * @param id The ID of the post to retrieve
     * @return The AuditionPost object corresponding to the given ID
     * @throws SystemException if there's an error fetching the post or if the post is not found
     * @author Nadeem Shaikh
     */
    public AuditionPost getPostById(final String id) {
        try {
            final ResponseEntity<AuditionPost> response = restTemplate.exchange(
                BASE_URL + "/posts/" + id,
                HttpMethod.GET,
                null,
                AuditionPost.class,
                id
            );
            final AuditionPost post = response.getBody();
            return post != null ? post : new AuditionPost();

        } catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException(CANNOT_FIND_POST + id, RESOURCE_NOT_FOUND,
                    404, e);
            } else {
                throw new SystemException(ERROR_FETCHING_POST + e.getMessage(), API_ERROR,
                    e.getStatusCode().value(), e);
            }
        }
    }

    /**
     * Retrieves comments for a specific post from the external API.
     *
     * @param postId The ID of the post to retrieve comments for
     * @return A list of Comment objects associated with the given post ID
     * @throws SystemException if there's an error fetching comments or if the post is not found
     * @author Nadeem Shaikh
     */
    public List<Comment> getCommentsByPostId(final String postId) {
        try {
            final ResponseEntity<List<Comment>> response = restTemplate.exchange(
                BASE_URL + "/comments?postId={postId}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                },
                postId
            );
            final List<Comment> commentList = response.getBody();
            return commentList != null ? commentList : new ArrayList<>();
        } catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException(CANNOT_FIND_COMMENTS + postId, RESOURCE_NOT_FOUND,
                    404, e);
            } else {
                throw new SystemException(ERROR_FETCHING_COMMENTS + postId + ": " + e.getMessage(),
                    API_ERROR, e.getStatusCode().value(), e);
            }

        }
    }

    /**
     * Retrieves comments for a specific post from the external API using the direct endpoint.
     *
     * @param postId The ID of the post to retrieve comments for
     * @return A list of Comment objects associated with the given post ID
     * @throws SystemException if there's an error fetching comments or if the post is not found
     * @author [Your Name]
     */
    public List<Comment> getCommentsForPost(final String postId) {
        try {
            final ResponseEntity<List<Comment>> response = restTemplate.exchange(
                BASE_URL + "/posts/{postId}/comments",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {},
                postId
            );
            final List<Comment> comments = response.getBody();
            return comments != null ? comments : new ArrayList<>();
        } catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException(CANNOT_FIND_COMMENTS + postId, RESOURCE_NOT_FOUND,
                    404, e);
            } else {
                throw new SystemException(ERROR_FETCHING_COMMENTS + e.getMessage(), API_ERROR,
                    e.getStatusCode().value(), e);
            }
        }
    }
}