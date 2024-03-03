package com.example.lab.constraint.validator;

import com.example.lab.constraint.UserStatusConstraint;
import com.example.lab.model.enumeration.UserStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class UserStatusValidator implements ConstraintValidator<UserStatusConstraint, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(s)) {
            return true;
        }

        try {
            UserStatus status = UserStatus.valueOf(s);
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
