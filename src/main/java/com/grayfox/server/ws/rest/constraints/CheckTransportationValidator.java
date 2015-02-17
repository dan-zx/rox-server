package com.grayfox.server.ws.rest.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.grayfox.server.route.RouteProvider;

public class CheckTransportationValidator implements ConstraintValidator<CheckTransportation, String> {

    @Override
    public void initialize(CheckTransportation constraintAnnotation) { }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        try {
            RouteProvider.Transportation.valueOf(value.trim().toUpperCase());
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}