package com.grayfox.server.ws.rest.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.grayfox.server.service.RecommenderService;

public class CheckTransportationValidator implements ConstraintValidator<CheckTransportation, String> {

    @Override
    public void initialize(CheckTransportation constraintAnnotation) { }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) return true;
        try {
            RecommenderService.Transportation.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}