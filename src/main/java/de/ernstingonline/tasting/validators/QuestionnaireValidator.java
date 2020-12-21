package de.ernstingonline.tasting.validators;

import javax.validation.constraints.NotEmpty;

public class QuestionnaireValidator {
    private String title;

    @NotEmpty(message = "Titel darf nicht leer sein")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
