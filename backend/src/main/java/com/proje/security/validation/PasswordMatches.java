package com.proje.security.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



import com.proje.security.validation.impl.PassMatcherValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PassMatcherValidator.class)
public @interface PasswordMatches {

    String message() default "Passwords don't matches";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}