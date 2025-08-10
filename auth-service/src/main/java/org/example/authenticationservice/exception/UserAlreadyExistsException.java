package org.example.authenticationservice.exception; // Or your exception package

import java.io.Serial;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UserAlreadyExistsException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UserAlreadyExistsException() {
        super("User already exists");
    }
}