package com.proje.security.validation.impl;


import com.proje.security.validation.PasswordMatches;
import com.proje.web.dto.CreateUser;
import com.proje.web.dto.UserDTO;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PassMatcherValidator implements ConstraintValidator<PasswordMatches, CreateUser> {

    @Override
    public boolean isValid(CreateUser value, ConstraintValidatorContext context) {

        return value.getPassword().equals(value.getRepassword());
    }



}