package com.epam.course.kubernetes.user.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epam.course.kubernetes.user.exception.UserValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserValidator {

    private static final String USERNAME_PATTERN =
            "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){1,28}[a-zA-Z0-9]$";

    public void validateUsername(String username) {
        Pattern pattern = Pattern.compile(USERNAME_PATTERN);
        Matcher matcher = pattern.matcher(username);
        if (!matcher.matches()) {
            log.error("Incorrect username: {}", username);
            throw new UserValidationException("Incorrect username: " + username);
        }
    }
}
