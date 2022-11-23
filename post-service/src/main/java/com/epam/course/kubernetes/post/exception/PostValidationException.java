package com.epam.course.kubernetes.post.exception;

public class PostValidationException extends RuntimeException implements PostException {

    public PostValidationException(String message) {
        super(message);
    }
}
