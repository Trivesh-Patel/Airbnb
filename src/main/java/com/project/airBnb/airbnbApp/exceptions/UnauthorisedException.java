package com.project.airBnb.airbnbApp.exceptions;

public class UnauthorisedException extends RuntimeException{

    public UnauthorisedException(String message) {
        super(message);
    }
}
