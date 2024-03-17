package com.example.lab.constraint.validator;

import com.example.lab.constraint.UserRoleConstraint;
import com.example.lab.model.enumeration.UserRoleEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class UserRoleValidator implements ConstraintValidator<UserRoleConstraint, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(s)) {
            return true;
        }

        try {
            UserRoleEnum role = UserRoleEnum.valueOf(s);
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
