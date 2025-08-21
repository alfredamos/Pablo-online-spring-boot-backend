package com.alfredamos.springbootbackend.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, Enum> {
    private List<String> acceptedValues;

    @Override
    public void initialize(ValueOfEnum annotation) {
        acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(Enum value, ConstraintValidatorContext context) {
        System.out.println("In enum-validator, value : " + value);
        if (value == null) {
            return true; // Consider if null is allowed or use @NotNull alongside
        }
        return acceptedValues.contains(value.toString());
    }
}
