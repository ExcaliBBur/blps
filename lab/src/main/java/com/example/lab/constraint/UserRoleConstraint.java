package com.example.lab.constraint;

import com.example.lab.constraint.validator.UserRoleValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserRoleValidator.class)
public @interface UserRoleConstraint {
    String message() default "";

    boolean canNull() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
