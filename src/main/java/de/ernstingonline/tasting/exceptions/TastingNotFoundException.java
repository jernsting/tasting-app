package de.ernstingonline.tasting.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Tasting not found")
public class TastingNotFoundException extends RuntimeException{
}
