package com.epam.course.kubernetes.user.exception;

public class UserValidationException extends RuntimeException implements UserException {

    public UserValidationException(String message) {
        super(message);
    }
}
