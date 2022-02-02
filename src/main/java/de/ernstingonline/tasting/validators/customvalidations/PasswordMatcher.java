package de.ernstingonline.tasting.validators.customvalidations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = PasswordMatcherValidator.class)
@Target({ElementType.TYPE})
@Retention(RUNTIME)
@Documented
public @interface PasswordMatcher {
    String message() default
            "Passwörter müssen übereinstimmen";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
