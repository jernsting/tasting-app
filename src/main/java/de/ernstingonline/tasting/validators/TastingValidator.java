package de.ernstingonline.tasting.validators;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class TastingValidator {
    private String name;
    private String date;


    @NotEmpty(message = "Bitte einen Namen angeben")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Pattern(regexp="^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$",
    message = "Bitte ein gültiges Datum im Format dd.mm.yyyy angeben.")
    @NotEmpty(message = "Bitte ein Datum angeben")
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
