package de.ernstingonline.tasting.validators;

import de.ernstingonline.tasting.validators.customValidations.PasswordMatcher;
import de.ernstingonline.tasting.validators.customValidations.UsernameTaken;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@PasswordMatcher()
@UsernameTaken()
public class PlayerValidator extends PasswordChanger {

    private String username;
    private String displayname;

    @NotEmpty(message = "Anzeigename darf nicht leer sein")
    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    @NotEmpty(message = "Username darf nicht leer sein.")
    @Size(min = 3, message = "Nutzername muss aus mindestens drei Zeichen bestehen")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
