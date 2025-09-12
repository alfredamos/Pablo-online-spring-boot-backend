package com.alfredamos.springbootbackend.exceptions;

public class InternalAuthenticationServiceException extends RuntimeException {
    public InternalAuthenticationServiceException(String message) {
        super(message);
    }
}
