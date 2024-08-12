package com.audition.web;

import com.audition.model.AuditionPost;
import com.audition.model.Comment;
import com.audition.service.AuditionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for handling Audition-related HTTP requests.
 *
 * @author Nadeem Shaikh
 */
@RestController
public class AuditionController {

    @Autowired
    private transient AuditionService auditionService;

    /**
     * Retrieves a list of AuditionPosts, optionally filtered by user ID.
     *
     * @param userId Optional user ID to filter posts
     * @return List of AuditionPost objects
     * @author Nadeem Shaikh
     */
    @RequestMapping(value = "/posts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<AuditionPost> getPosts(@RequestParam(required = false) final Integer userId) {
        List<AuditionPost> allPosts = auditionService.getPosts();
        if (userId != null) {
            allPosts = allPosts.stream()
                .filter(post -> post.getUserId() == userId)
                .collect(Collectors.toList());
        }
        return allPosts;
    }

    /**
     * Retrieves a specific AuditionPost by its ID.
     *
     * @param postId The ID of the post to retrieve
     * @return ResponseEntity containing the AuditionPost
     * @throws ResponseStatusException if the post ID is blank or the post is not found
     * @author Nadeem Shaikh
     */
    @RequestMapping(value = "/posts/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuditionPost> getPosts(@PathVariable("id") final String postId) {
        if (StringUtils.isBlank(postId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Post ID cannot be null or empty");
        }
        
        final AuditionPost auditionPost = auditionService.getPostById(postId);
        if (auditionPost == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
        
        return ResponseEntity.ok(auditionPost);
    }

    /**
     * Retrieves a list of Comments for a specific post.
     *
     * @param postId The ID of the post to retrieve comments for
     * @return ResponseEntity containing a list of Comment objects
     * @throws ResponseStatusException if the post ID is blank
     * @author Nadeem Shaikh
     */
    @GetMapping("/posts/{postId}/comments")
    public final ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable final String postId) {
        if (StringUtils.isBlank(postId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Post ID cannot be null or empty");
        }

        final List<Comment> comments = auditionService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }


}