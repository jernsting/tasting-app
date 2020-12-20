package de.ernstingonline.tasting.validators.customValidations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = UsernameTakenValidator.class)
@Target({ElementType.TYPE})
@Retention(RUNTIME)
@Documented
public @interface UsernameTaken {
    String message() default
            "Nutzername bereits vergeben";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
