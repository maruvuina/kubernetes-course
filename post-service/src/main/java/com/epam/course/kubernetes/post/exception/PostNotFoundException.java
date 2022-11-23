package com.epam.course.kubernetes.post.exception;

public class PostNotFoundException extends RuntimeException implements PostException {

    public PostNotFoundException(String message) {
        super(message);
    }
}
