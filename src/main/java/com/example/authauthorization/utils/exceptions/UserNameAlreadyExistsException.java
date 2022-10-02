package com.example.authauthorization.utils.exceptions;

public class UserNameAlreadyExistsException extends RuntimeException{

    public UserNameAlreadyExistsException(String message) {
        super(message);
    }
}
