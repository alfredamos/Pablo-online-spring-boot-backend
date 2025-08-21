package com.alfredamos.springbootbackend.auth;

import lombok.Getter;

@Getter
public class AuthParams {
    public final static String accessToken = "accessToken";
    public final static String refreshToken = "refreshToken";
    public final static String authorization = "Authorization";
    public final static String bearer = "Bearer ";
    public final static String role = "ROLE_";
    public final static String accessTokenPath = "/";
    public final static String refreshTokenPath = "/api/auth/refresh";

}
