package de.ernstingonline.tasting.validators;

import de.ernstingonline.tasting.validators.customvalidations.PasswordMatcher;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@PasswordMatcher
public abstract class PasswordChanger {
    private String password;
    private String password2;

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
}
