package com.alfredamos.springbootbackend.auth;

import com.alfredamos.springbootbackend.exceptions.UnAuthorizedException;
import com.alfredamos.springbootbackend.users.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@AllArgsConstructor
@Service
public class JwtService {
    private final JwtConfig jwtConfig;

    public Jwt generateAccessToken(User user) {
        return generateToken(user, jwtConfig.getAccessTokenExpiration());

    }

    public Jwt generateRefreshToken(User user) {
        return generateToken(user, jwtConfig.getRefreshTokenExpiration());

    }

    public Jwt parseToken(String token){
        try{
            var claims = getClaims(token);

            return new Jwt(claims, jwtConfig.getSecretKey());
        }catch (JwtException ex){
            throw new UnAuthorizedException(ex.getMessage());
        }
    }

    private Jwt generateToken(User user, long tokenExpiration) {
        int milliSeconds = 1000;
        var claims = Jwts.claims()
                .subject(user.getId() == null ? "" : user.getId().toString())
                .add("email", user.getEmail())
                .add("name", user.getName())
                .add("role", user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + milliSeconds * tokenExpiration))
                .build();


        return new Jwt(claims, jwtConfig.getSecretKey());
    }

    private Claims getClaims(String token){
        if (token.isEmpty()) {
            throw new UnAuthorizedException("Invalid credential!");

        }

        return (Claims) Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}

