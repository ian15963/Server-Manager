package com.server.application.service.validation;


import com.server.application.dto.UserInsertDTO;
import com.server.application.resource.exceptions.FieldMessage;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserInsertDTO> {



    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }
    @Override
    public boolean isValid(UserInsertDTO user, ConstraintValidatorContext context) {

        List<FieldMessage> list = new ArrayList<>();

        if (!user.getPassword().equals(user.getMatchingPassword())) {
            list.add(new FieldMessage("matchingPassword", "As senhas não são iguais"));
        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}
