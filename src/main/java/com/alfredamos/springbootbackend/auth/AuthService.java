package com.alfredamos.springbootbackend.auth;

import com.alfredamos.springbootbackend.auth.dto.ChangePasswordRequest;
import com.alfredamos.springbootbackend.auth.dto.EditProfileRequest;
import com.alfredamos.springbootbackend.auth.dto.LoginRequest;
import com.alfredamos.springbootbackend.auth.dto.SignupRequest;
import com.alfredamos.springbootbackend.exceptions.BadRequestException;
import com.alfredamos.springbootbackend.exceptions.NotFoundException;
import com.alfredamos.springbootbackend.exceptions.UnAuthorizedException;
import com.alfredamos.springbootbackend.users.*;
import com.alfredamos.springbootbackend.utils.ResponseMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public ResponseMessage login(LoginRequest loginRequest, HttpServletResponse response) {
        //----> Authenticate user.
        this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        //----> Get the authenticated user.
        var user = this.userService.getUserByEmail(loginRequest.getEmail());

        //----> Get access token.
        var accessToken = this.jwtService.generateAccessToken(user);

        //----> Put the access-token in the access-cookie.
        var accessCookie = makeCookie(new CookieParameter(AuthParams.accessToken, accessToken, (int)this.jwtConfig.getAccessTokenExpiration(), AuthParams.accessTokenPath
        ));

        //----> Add access-cookie to a response object.
        response.addCookie(accessCookie);

        //----> Get refresh-token
        var refreshToken = this.jwtService.generateRefreshToken(user);

        //----> Put the refresh-token in refresh-cookie.
        var refreshCookie = makeCookie(new CookieParameter(AuthParams.refreshToken, refreshToken, (int)this.jwtConfig.getRefreshTokenExpiration(), AuthParams.refreshTokenPath
        ));

        //----> Add refresh-cookie to a response object.
        response.addCookie(refreshCookie);

        return new ResponseMessage("Success", "Login is successful!", 200);
    }

    public ResponseMessage changePassword(ChangePasswordRequest changePasswordRequest) {
        //----> Extract email,oldPassword, confirmPassword and newPassword.
        var email = changePasswordRequest.getEmail();
        var oldPassword = changePasswordRequest.getOldPassword();
        var confirmPassword = changePasswordRequest.getConfirmPassword();
        var newPassword = changePasswordRequest.getNewPassword();

        //----> Check for match between confirm-password and new-password.
        checkPasswordMatch(newPassword, confirmPassword);

        //----> Check for the existence of user.
        var user = foundUserByEmail(email, AuthActionType.edit);

        //----> Check for correct password.
        checkForCorrectPassword(oldPassword, user.getPassword());

        //----> Hash password.
        var hashedPassword = this.passwordEncoder.encode(newPassword);

        //----> Replace the old hashed password with new one.
        user.setPassword(hashedPassword);

        //----> Save the new password in the database.
        authRepository.save(user);


        return new ResponseMessage("Success", "Password has been changed successfully!", 200);
    }

    public ResponseMessage editProfile(EditProfileRequest editProfileRequest) {
        //----> Get the email and password from the payload.
        var email = editProfileRequest.getEmail();
        var password = editProfileRequest.getPassword();

        //----> Check for the existence of user.
        var user = foundUserByEmail(email, AuthActionType.edit);

        //----> Check for correct password.
        checkForCorrectPassword(password, user.getPassword());

        //----> save the change in profile into the database.
        editProfileRequest.setPassword(user.getPassword());
        var userPayload = this.authMapper.toEntity(editProfileRequest);
        //var userPayload = this.getUserFromEditProfile(editProfile, user);
        userPayload.setId(user.getId());
        authRepository.save(userPayload);

        return new ResponseMessage("Success", "Your profile is edited successfully!", 200);

    }

    public ResponseMessage signup(SignupRequest signupRequest) {
        //----> Get the email, password and confirm-password from the payload.
        var email = signupRequest.getEmail();
        var password = signupRequest.getPassword();
        var confirmPassword = signupRequest.getConfirmPassword();

        //----> Check for match between confirm-password and password.
        checkPasswordMatch(password, confirmPassword);

        //----> Check for the existence of user.
        foundUserByEmail(email, AuthActionType.create);

        //----> Check for
        String hashedPassword = passwordEncoder.encode(password);
        System.out.println("Hashed password: " + hashedPassword);

        //----> Set the hashed password on signup.
        signupRequest.setPassword(hashedPassword);

        //----> Get user payload from signup.
        var userPayload = this.authMapper.toEntity(signupRequest);
        System.out.println("userPayload: " + userPayload);

        //----> Save the new user in the database.
        authRepository.save(userPayload);

        // Save username and hashedPassword to your database
        return new ResponseMessage("Success", "Signup is successful!", 201);
    }

    public void removeLoginAccess(HttpServletResponse response){
        //----> Remove accessToken
        var accessCookie = makeCookie(new CookieParameter(AuthParams.accessToken, null, 0, AuthParams.accessTokenPath));

        //----> Add access-cookie to a response object.
        response.addCookie(accessCookie);


        //----> Remove refresh-cookie.
        var refreshCookie = makeCookie(new CookieParameter(AuthParams.refreshToken, null, 0, AuthParams.refreshTokenPath));

        //----> Add refresh-cookie to a response object.
        response.addCookie(refreshCookie);

    }

    public UserDto getCurrentUser(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        var email = (String) authentication.getPrincipal();
        var userDto = this.userMapper.toDTO(this.userService.getUserByEmail(email));

        if (userDto == null){
            throw  new NotFoundException("Current user is not found!");
        }

        return userDto;
    }

    public String getRefreshToken(String refreshToken, HttpServletResponse response){
        var jwt = jwtService.parseToken(refreshToken);

        if (jwt == null || jwt.isExpired()){
            throw new UnAuthorizedException("Invalid credentials!");
        }

        var user = this.userRepository.findById(jwt.getUserId()).orElseThrow();

        var accessToken = jwtService.generateAccessToken(user);

        //----> Put the access-token in the access-cookie.
        var accessCookie = makeCookie(new CookieParameter(AuthParams.accessToken, accessToken, (int)this.jwtConfig.getAccessTokenExpiration(), AuthParams.accessTokenPath
        ));

        response.addCookie(accessCookie);

        return  accessToken.toString();
    }

    private Cookie makeCookie(CookieParameter  cookieParameter){
        //----> Set cookie.
        var cookie = new Cookie(cookieParameter.getCookieName(), cookieParameter.getCookieValue() == null ? null : cookieParameter.getCookieValue().toString());

        cookie.setHttpOnly(true);
        cookie.setPath(cookieParameter.getCookiePath());
        cookie.setMaxAge(cookieParameter.getExpiration());
        cookie.setSecure(false);

        return  cookie;
    }

    private void checkPasswordMatch(String password, String confirmPassword){
        //----> Check for match between confirm-password and password.
        var isMatch = confirmPassword.equals(password);
        System.out.println("isMatch: " + isMatch);

        if (!isMatch){
            //throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must match!");
            throw new BadRequestException("Password must match!");
        }
    }

    private void checkForCorrectPassword(CharSequence rawPassword, String storedPassword){
        var isCorrectPassword = this.passwordEncoder.matches(rawPassword, storedPassword);
        if (!isCorrectPassword){
            throw new UnAuthorizedException("Invalid credential!");
        }
    }

    private User foundUserByEmail(String email, String mode){
        var user = this.authRepository.findUserByEmail(email);
        if (mode.equalsIgnoreCase(AuthActionType.edit)) {
            if (user == null) {
                throw new NotFoundException("Invalid credential!");
            }
        } else if (mode.equalsIgnoreCase(AuthActionType.create)) {
            if (user != null) {
                throw new UnAuthorizedException("Invalid credential!");
            }
        }

        return user;
    }

}


