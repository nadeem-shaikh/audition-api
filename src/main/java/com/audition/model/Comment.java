package com.audition.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a comment in the audition system.
 *
 * @author Nadeem Shaikh
 */
@Getter
@Setter
public class Comment {
    private int id;
    private int postId;
    private String name;
    private String email;   
    private String body;

    /**
     * Default constructor for Comment.
     *
     * @author Nadeem Shaikh
     */
    public Comment() {

    }

    /**
     * Constructs a Comment with specified id and body.
     *
     * @param id The unique identifier for the comment
     * @param body The content of the comment
     * @author Nadeem Shaikh
     */
    public Comment(final int id, final String body) {    
        this.id = id;
        this.body = body;
    }

    /**
     * Returns a string representation of the Comment object.
     *
     * @return A string representation of the Comment
     * @author Nadeem Shaikh
     */
    @Override
    public String toString() {
        return "Comment{"
                + "id=" + id 
                + ", postId=" + postId
                + ", name='" + name + '\''
                + ", email='" + email + '\''
                + ", body='" + body + '\''
                + '}';
    }
}