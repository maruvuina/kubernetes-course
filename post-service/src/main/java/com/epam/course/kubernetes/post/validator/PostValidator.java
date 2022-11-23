package com.epam.course.kubernetes.post.validator;

import com.epam.course.kubernetes.post.exception.PostValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PostValidator {

    public void validateText(String text) {
        if (text.length() > 2000) {
            log.error("Text length greater then 2000 symbols. Current length: {}", text.length());
            throw new PostValidationException("Text length greater then 2000 symbols. Current length: " + text.length());
        }
    }
}
