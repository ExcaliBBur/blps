package com.example.lab.constraint.validator;

import com.example.lab.constraint.UserRoleConstraint;
import com.example.lab.model.enumeration.UserRole;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserRoleValidator implements ConstraintValidator<UserRoleConstraint, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            UserRole role = UserRole.valueOf(s);
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
