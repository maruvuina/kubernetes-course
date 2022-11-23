package com.epam.course.kubernetes.user.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = IdValidator.class)
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidId {

    String message() default "Incorrect id";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
