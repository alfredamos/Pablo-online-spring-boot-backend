package com.alfredamos.springbootbackend.users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsUserByEmail(String email);

    User findUserByEmail(String email);

    User getUserByEmail(String email);
}
