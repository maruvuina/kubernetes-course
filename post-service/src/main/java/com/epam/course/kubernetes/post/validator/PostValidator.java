package com.epam.course.kubernetes.post.validator;

import com.epam.course.kubernetes.post.dto.PostDto;
import com.epam.course.kubernetes.post.dto.PostUpdate;
import com.epam.course.kubernetes.post.exception.PostValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PostValidator {

    public void validate(PostDto postDto) {
        validateTopic(postDto.getTopic());
        validateText(postDto.getText());
    }

    public void validate(PostUpdate postUpdate) {
        validateTopic(postUpdate.getTopic());
        validateText(postUpdate.getText());
    }

    private void validateText(String text) {
        int maxLength = 2000;
        if (text.length() > maxLength) {
            log.error("Text length greater then 2000 symbols. Current length: {}", text.length());
            throw new PostValidationException("Text length greater then 2000 symbols. Current length: " + text.length());
        }
    }

    private void validateTopic(String topic) {
        int maxLength = 100;
        if (topic.length() > maxLength) {
            log.error("Text length greater then 100 symbols. Current length: {}", topic.length());
            throw new PostValidationException("Text length greater then 100 symbols. Current length: " + topic.length());
        }
    }
}
