package com.alfredamos.springbootbackend.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var cookies = request.getCookies(); //----> Get all cookies.
        var accessToken = mySpecificCookieValue(cookies); //----> Get access token

        var requestURI = request.getRequestURI(); //----> Get current uri.

        //----> Check token only for non-public routes.
        if(!publicRoutes().contains(requestURI)) {
            var jwt = jwtService.parseToken(accessToken);

            //----> Check for null and expired jwt object.
            if (jwt == null || jwt.isExpired()) {
                filterChain.doFilter(request, response);
                return;
            }

            var role = jwt.getUserRole(); //----> Get the role of the current user.
            var email = jwt.getUserEmail(); //----> Get the email of current user.

            //----> Authenticate the current user.
            var authentication = new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    List.of(new SimpleGrantedAuthority(AuthParams.role + role))
            );

            //----> Set authentication details.
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            //----> Update security context info.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);


    }

    private String mySpecificCookieValue(Cookie[] cookies){
        if(cookies == null) return "";

        return Stream.of(cookies)
                .filter(cookie -> cookie.getName().equals(AuthParams.accessToken))
                .map(Cookie::getValue)
                .findFirst().orElse(null);

    }

    private List<String> publicRoutes(){
        return Arrays.asList("/api/auth/login", "/api/auth/refresh", "/api/auth/signup", "/api/orders/webhook", "/api/menu-items");

    }

}