package com.grayfox.server.ws.rest.constraints;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target(PARAMETER)
@Retention(RUNTIME)
@Constraint(validatedBy = CheckTransportationValidator.class)
@Documented
public @interface CheckTransportation {

    String message() default "transportation.invalid.error";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}