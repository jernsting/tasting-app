package de.ernstingonline.tasting.validators.customValidations;

import de.ernstingonline.tasting.db.dao.game.PlayerDao;
import de.ernstingonline.tasting.db.entities.game.Player;
import de.ernstingonline.tasting.validators.PlayerValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class UsernameTakenValidator implements ConstraintValidator<UsernameTaken, Object>  {

    @Autowired
    private PlayerDao playerDao;

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext context) {
        if (!(o instanceof PlayerValidator))
            throw new IllegalArgumentException(
                    "Illegal method signature, expected PlayerValidator."
            );

        PlayerValidator playerValidator = (PlayerValidator) o;
        List<Player> player = playerDao.findByUsername(playerValidator.getUsername());
        boolean isValid = player.isEmpty();

        if(!isValid){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode( "username" ).addConstraintViolation();
        }

        return isValid;
    }
}
