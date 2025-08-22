package com.alfredamos.springbootbackend.users;

import com.alfredamos.springbootbackend.exceptions.ForbiddenException;
import com.alfredamos.springbootbackend.exceptions.NotFoundException;
import com.alfredamos.springbootbackend.utils.ResponseMessage;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapperImpl;

    //----> Delete a resource with given id.
    public ResponseMessage deleteUserById(UUID id, boolean canDeleteAndView)  {
        //----> Check for ownership or admin privilege.
        if (!canDeleteAndView) {
            throw new ForbiddenException("You don't have permission to remove this order");
        }

        //-----> Delete resource.
        userRepository.deleteById(id);

        return new ResponseMessage("Success", "User is deleted successfully!", 404);

    }

    //----> Get all users.
    public List<UserDto> getAllUsers(){
        var users = userRepository.findAll();

        return this.userMapperImpl.toDTOList(users);
    }

    //----> Get user by id.
    public UserDto getUserById(UUID id, boolean canDeleteAndView)  {
        //----> Check for ownership or admin privilege.
        if (!canDeleteAndView) {
            throw new ForbiddenException("You don't have permission to view this order");
        }

        //----> Get the user by id.
        var user =  this.userRepository.findById(id).orElse(null);

        return this.userMapperImpl.toDTO(user);
    }

    //----> Get user by email.
    public User getUserByEmail(String email)  {
        //----> Get the user with the given email
        var user = this.userRepository.findUserByEmail(email);

        //----> Check for existence of user.
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist!");
        }

        //----> Send back response.
        return user;
    }

}

