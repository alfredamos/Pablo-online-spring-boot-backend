package com.alfredamos.springbootbackend.controllers;

import com.alfredamos.springbootbackend.exceptions.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerErrorException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleErrors(MethodArgumentNotValidException exception){
        var errors = new HashMap<String, String>();

        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleSecurityException(Exception ex) {
        ProblemDetail errorDetail = null;

        // TODO send this stack trace to an observability tool
        //exception.printStackTrace()

        //----> Bad credentials.
        if (ex instanceof BadCredentialsException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), ex.getMessage());
            errorDetail.setProperty("description", "The username or password is incorrect");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetail);
        }

        //----> Account status expiration.
        if (ex instanceof AccountStatusException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), ex.getMessage());
            errorDetail.setProperty("description", "The account is locked");

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetail);
        }

        //----> Authentication exception
        if (ex instanceof AuthenticationException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), ex.getMessage());
            errorDetail.setProperty("description", "Invalid credentials");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetail);
        }

        //----> Access denial exception
        if (ex instanceof AccessDeniedException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), ex.getMessage());
            errorDetail.setProperty("description", "You are not authorized to access this resource");

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetail);
        }

        //----> Account status expiration.
        if (ex instanceof InternalAuthenticationServiceException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), "Internal authentication issues with login credentials!");
            errorDetail.setProperty("description", "Invalid credentials");

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetail);
        }

        if (ex instanceof SignatureException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), ex.getMessage());
            errorDetail.setProperty("description", "The JWT signature is invalid");

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetail);
        }

        if (ex instanceof ExpiredJwtException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), ex.getMessage());
            errorDetail.setProperty("description", "The JWT token has expired");

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetail);
        }

        if (ex instanceof BadRequestException){
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), ex.getMessage());
            errorDetail.setProperty("description", "Please provide all the necessary values.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetail);
        }

        if (ex instanceof ForbiddenException){
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), ex.getMessage());
            errorDetail.setProperty("description", "You are not permitted to view, delete and edit this resource.");

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetail);
        }

        if (ex instanceof NotFoundException){
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(404), ex.getMessage());
            errorDetail.setProperty("description", "This resource is not available!");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetail);
        }

        if (ex instanceof UnAuthorizedException){
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), ex.getMessage());
            errorDetail.setProperty("description", "Invalid credentials!");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetail);
        }

        if (ex instanceof PaymentException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500), ex.getMessage());
            errorDetail.setProperty("description", ex.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetail);
        }

        if (ex instanceof ServerErrorException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500), ex.getMessage());
            errorDetail.setProperty("description", "Unknown internal server error.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetail);
        }

        errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500), ex.getMessage());
        errorDetail.setProperty("description", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetail);
    }


}
