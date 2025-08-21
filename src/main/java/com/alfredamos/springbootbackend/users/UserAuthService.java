package com.alfredamos.springbootbackend.users;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@AllArgsConstructor
@Service
public class UserAuthService implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = this.userService.getUserByEmail(email);

        return new User(
                user.getEmail(),
                user.getPassword(),
                Collections.emptyList()
        );

    }

}

