package com.epam.course.kubernetes.user.exception;

public class UserNotFoundException extends RuntimeException implements UserException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
