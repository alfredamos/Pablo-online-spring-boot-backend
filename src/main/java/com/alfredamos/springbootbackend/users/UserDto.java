package com.alfredamos.springbootbackend.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private UUID id;
    private String address;

    private String name;

    private String email;

    private String phone;

    private Gender gender;

    private String image;

    private Role role;

}
