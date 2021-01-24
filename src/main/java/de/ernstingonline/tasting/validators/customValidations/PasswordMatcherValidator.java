package de.ernstingonline.tasting.validators.customValidations;

import de.ernstingonline.tasting.validators.PasswordChanger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatcherValidator implements ConstraintValidator<PasswordMatcher, Object> {

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext context) {
        if (!(o instanceof PasswordChanger)){
            throw new IllegalArgumentException(
                    "Illegal method signature, expected PlayerValidator.");
        }
        PasswordChanger playerValidator = (PasswordChanger) o;
        boolean isValid = playerValidator.getPassword().equals(playerValidator.getPassword2());

        if(!isValid){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode( "password" ).addConstraintViolation();
        }

        return isValid;
    }
}
