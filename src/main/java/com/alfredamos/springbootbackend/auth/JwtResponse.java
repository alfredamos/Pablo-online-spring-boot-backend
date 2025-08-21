package com.alfredamos.springbootbackend.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class JwtResponse {
    private String token;
}
