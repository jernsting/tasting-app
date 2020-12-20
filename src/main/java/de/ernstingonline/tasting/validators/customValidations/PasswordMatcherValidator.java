package de.ernstingonline.tasting.validators.customValidations;

import de.ernstingonline.tasting.validators.PlayerValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatcherValidator implements ConstraintValidator<PasswordMatcher, Object> {

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext context) {
        if (!(o instanceof PlayerValidator)){
            throw new IllegalArgumentException(
                    "Illegal method signature, expected PlayerValidator.");
        }
        PlayerValidator playerValidator = (PlayerValidator) o;
        boolean isValid = playerValidator.getPassword().equals(playerValidator.getPassword2());

        if(!isValid){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode( "password" ).addConstraintViolation();
        }

        return isValid;
    }
}
