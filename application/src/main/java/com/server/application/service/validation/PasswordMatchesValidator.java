package com.server.application.service.validation;


import com.server.application.dto.UserInsertDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserInsertDTO> {



    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }
    @Override
    public boolean isValid(UserInsertDTO user, ConstraintValidatorContext context){

        return user.getPassword().equals(user.getMatchingPassword());

    }
}
