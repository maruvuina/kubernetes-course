package com.epam.course.kubernetes.post.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IdValidator implements ConstraintValidator<ValidId, Long> {

    @Override
    public void initialize(ValidId validId) {
    }

    @Override
    public boolean isValid(Long idValue, ConstraintValidatorContext cxt) {
        return idValue != null && idValue > 0;
    }

}
