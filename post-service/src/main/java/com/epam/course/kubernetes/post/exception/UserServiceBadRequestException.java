package com.epam.course.kubernetes.post.exception;

public class UserServiceBadRequestException extends RuntimeException implements UserServiceException {

    public UserServiceBadRequestException(String message) {
        super(message);
    }
}
