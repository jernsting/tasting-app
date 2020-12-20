package de.ernstingonline.tasting.validators;

import javax.validation.constraints.NotEmpty;

public class InviteValidator {
    private String invite;

    @NotEmpty(message = "Einladungscode darf nicht leer sein")
    public String getInvite() {
        return invite;
    }

    public void setInvite(String invite) {
        this.invite = invite;
    }
}
