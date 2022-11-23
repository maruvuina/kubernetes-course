package com.epam.course.kubernetes.post.exception;


public class UserServiceServerErrorException extends RuntimeException implements UserServiceException {

    public UserServiceServerErrorException(String message) {
        super(message);
    }
}
