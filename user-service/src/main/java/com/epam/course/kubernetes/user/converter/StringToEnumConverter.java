package com.epam.course.kubernetes.user.converter;

import com.epam.course.kubernetes.user.exception.ConverterException;
import com.epam.course.kubernetes.user.model.PostAmountStatus;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j
public class StringToEnumConverter implements Converter<String, PostAmountStatus> {

    @Override
    public PostAmountStatus convert(String source) {
        try {
            return PostAmountStatus.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException ex) {
            log.error("Cannot convert enum value to string");
            throw new ConverterException(ex.getMessage());
        }
    }

}
