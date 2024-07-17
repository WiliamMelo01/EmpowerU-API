package org.wiliammelo.empoweru.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = OneRightOptionValidator.class)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface OneRightOption {
    String message() default "Each question must have exactly one right answer";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

