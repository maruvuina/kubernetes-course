package com.epam.course.kubernetes.user.exception;

public class UserUpdateException extends RuntimeException implements UserException {

    public UserUpdateException(String message) {
        super(message);
    }
}
