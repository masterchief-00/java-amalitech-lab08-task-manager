package com.kwizera.Exceptions;

public class DuplicateEmailException extends Exception {
    public DuplicateEmailException(String message) {
        super(message);
    }
}
