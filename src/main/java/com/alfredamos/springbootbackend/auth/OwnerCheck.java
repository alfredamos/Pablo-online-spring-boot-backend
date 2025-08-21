package com.alfredamos.springbootbackend.auth;

import com.alfredamos.springbootbackend.users.Role;
import com.alfredamos.springbootbackend.users.User;
import com.alfredamos.springbootbackend.users.UserRepository;
import com.alfredamos.springbootbackend.users.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class OwnerCheck {
    private final UserRepository userRepository;

    public boolean compareAuthUserIdWithParamUserId(UUID userId){
        //----> Get the user id from security context.
        var idOfUser = getUserIdFromContext();

        //----> Compare the two user id for equality.
        return idOfUser.equals(userId);

    }

    public boolean compareAuthUserIdWithUserIdOnOrder(UUID userIdFromOrder){
        //----> Get the user id from security context.
        var idOfUser = getUserIdFromContext();

        //----> Compare the two user id for equality.
        return idOfUser.equals(userIdFromOrder);
    }

    public boolean isAdminUser(){
        //----> Check for admin role.
        return getCurrentUser().getRole().equals(Role.Admin);
    }

    private UUID getUserIdFromContext(){
        //----> Get user id.
        return getCurrentUser().getId();
    }

    private User getCurrentUser(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var email = (String) authentication.getPrincipal();
        return userRepository.getUserByEmail(email);
    }
}
