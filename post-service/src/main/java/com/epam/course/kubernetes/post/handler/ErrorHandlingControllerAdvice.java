package com.epam.course.kubernetes.post.handler;

import java.time.Instant;
import java.util.Arrays;

import javax.validation.ConstraintViolationException;

import com.epam.course.kubernetes.post.exception.PostNotFoundException;
import com.epam.course.kubernetes.post.exception.UserServiceBadRequestException;
import com.epam.course.kubernetes.post.exception.UserServiceNotFoundException;
import com.epam.course.kubernetes.post.exception.UserServiceServerErrorException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ErrorHandlingControllerAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handlePostNotFoundException(PostNotFoundException ex) {
        return handlePostException(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleUserServiceBadRequestException(UserServiceBadRequestException ex) {
        return handleCommonException(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleUserServiceServerErrorException(UserServiceServerErrorException ex) {
        return handleCommonException(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleUserServiceNotFoundException(UserServiceNotFoundException ex) {
        return handleCommonException(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleInvalidFormatException(InvalidFormatException ex) {
        return handleCommonException(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        return handleCommonException(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return handleCommonException(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleJsonMappingException(JsonMappingException ex) {
        return handleCommonException(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleWebExchangeBindException(WebExchangeBindException ex) {
        return handleCommonException(ex, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse> handlePostException(Exception ex, HttpStatus httpStatus) {
        return handleException(ex, httpStatus);
    }

    private ResponseEntity<ErrorResponse> handleCommonException(Exception ex, HttpStatus httpStatus) {
        log.error(ex.getMessage());
        return handleException(ex, httpStatus);
    }

    private ResponseEntity<ErrorResponse> handleException(Exception ex, HttpStatus httpStatus) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .timestamp(Instant.now())
                        .error(httpStatus.getReasonPhrase())
                        .message(ex.getMessage())
                        .trace(Arrays.toString(ex.getStackTrace()))
                        .build(),
                httpStatus);
    }
}
