package com.alfredamos.springbootbackend.auth;

import com.alfredamos.springbootbackend.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthRepository extends JpaRepository<User, UUID> {
    User findUserByEmail(String email);
}
