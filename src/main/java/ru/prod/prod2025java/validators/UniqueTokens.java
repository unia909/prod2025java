package ru.prod.prod2025java.validators;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.util.List;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@java.lang.annotation.Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueTokens.Validator.class)
public @interface UniqueTokens {
    String message() default "Invalid payload";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<UniqueTokens, List<String>> {
        @Override
        public void initialize(UniqueTokens constraintAnnotation) {
        }

        @Override
        public boolean isValid(List<String> target, ConstraintValidatorContext constraintValidatorContext) {
            if (target != null) {
                for (String token : target) {
                    if (token.length() < 3 || token.length() > 30) {
                        return false;
                    }
                }
            }
            return true;
        }
    }
}
