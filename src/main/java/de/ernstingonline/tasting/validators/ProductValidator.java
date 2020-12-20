package de.ernstingonline.tasting.validators;

import javax.validation.constraints.NotEmpty;

public class ProductValidator {
    private String name;

    @NotEmpty(message = "Produktname darf nicht leer sein!")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
