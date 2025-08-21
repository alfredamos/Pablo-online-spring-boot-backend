package com.alfredamos.springbootbackend.auth;

import com.alfredamos.springbootbackend.auth.dto.EditProfileRequest;
import com.alfredamos.springbootbackend.auth.dto.SignupRequest;
import com.alfredamos.springbootbackend.users.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    User toEntity(SignupRequest signup);
    User toEntity(EditProfileRequest editProfile);

}

