package com.alfredamos.springbootbackend.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ForbiddenException extends RuntimeException{
    public ForbiddenException(String message) {
        super(message);
    }
}