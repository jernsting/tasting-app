package de.ernstingonline.tasting.validators;

import de.ernstingonline.tasting.validators.customValidations.PasswordMatcher;
import de.ernstingonline.tasting.validators.customValidations.UsernameTaken;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@PasswordMatcher()
@UsernameTaken()
public class PlayerValidator {

    private String username;
    private String password;
    private String password2;
    private String displayname;

    @NotEmpty(message = "Anzeigename darf nicht leer sein")
    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    @NotEmpty(message = "Passwort wiederholung darf nicht leer sein")
    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    @NotEmpty(message = "Passwort darf nicht leer sein")
    @Size(min = 6, message = "Passwort muss aus mindestens 6 Zeichen bestehen")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
