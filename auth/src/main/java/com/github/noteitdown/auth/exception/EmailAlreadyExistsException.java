package com.github.noteitdown.auth.exception;


public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException() {
        super("That email address is already in use!");
    }
}
