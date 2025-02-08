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
@Constraint(validatedBy = Categories.Validator.class)
public @interface Categories {
    String message() default "Invalid payload";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<Categories, List<String>> {
        @Override
        public void initialize(Categories constraintAnnotation) {
        }

        @Override
        public boolean isValid(List<String> list, ConstraintValidatorContext constraintValidatorContext) {
            if (list == null) {
                return true;
            }
            if (list.size() < 2 || list.size() > 20) {
                return false;
            }
            for (String category : list) {
                if (category.length() < 2 || category.length() > 20) {
                    return false;
                }
            }
            return true;
        }
    }
}
