package com.alfredamos.springbootbackend.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UnAuthorizedException extends RuntimeException{

    public UnAuthorizedException(String message) {
        super(message);
    }
}