package com.epam.course.kubernetes.post.exception;

public class UserServiceNotFoundException extends RuntimeException implements UserServiceException {

    public UserServiceNotFoundException(String message) {
        super(message);
    }

}
