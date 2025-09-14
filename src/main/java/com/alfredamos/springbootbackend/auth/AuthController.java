package com.alfredamos.springbootbackend.auth;

import com.alfredamos.springbootbackend.auth.dto.ChangePasswordRequest;
import com.alfredamos.springbootbackend.auth.dto.EditProfileRequest;
import com.alfredamos.springbootbackend.auth.dto.LoginRequest;
import com.alfredamos.springbootbackend.auth.dto.SignupRequest;
import com.alfredamos.springbootbackend.users.UserDto;
import com.alfredamos.springbootbackend.utils.ResponseMessage;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final OwnerCheck ownerCheck;


    @PatchMapping("/change-password")
    public ResponseEntity<ResponseMessage> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest){
        var result =  this.authService.changePassword(changePasswordRequest);

        return  new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("/edit-profile")
    public ResponseEntity<ResponseMessage> editProfile(@Valid @RequestBody EditProfileRequest editProfileRequest){
        var result = this.authService.editProfile(editProfileRequest);

        return  new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseMessage> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response){

        var loginResponse = this.authService.login(loginRequest, response);

        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);

    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@CookieValue(value = AuthParams.refreshToken) String refreshToken, HttpServletResponse response){
        var accessToken = this.authService.getRefreshToken(refreshToken, response); //----> Get access token.

        return ResponseEntity.ok(new JwtResponse(accessToken));
    }


    @PostMapping("/signup")
    public ResponseEntity<ResponseMessage> signup(@Valid @RequestBody SignupRequest signupRequest){
        System.out.println("In auth-controller, signup request: " + signupRequest);

        var result = this.authService.signup(signupRequest);

        return  new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(){
        var userDto = this.authService.getCurrentUser();

        return ResponseEntity.ok(userDto);
    }

}

