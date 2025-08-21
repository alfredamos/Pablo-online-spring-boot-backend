package com.alfredamos.springbootbackend.users;

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
    public ResponseMessage deleteUser(UUID id)  {
        checkForOrderExistence(id); //----> Check for existence of user with the given id.

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
    public UserDto getUserById(UUID id)  {
        checkForOrderExistence(id); //----> Check for existence of user with the given id.

        //----> Get the user by id.
        var user =  this.userRepository.findById(id).orElse(null);

        return this.userMapperImpl.toDTO(user);
    }

    //----> Get user by email.
    public User getUserByEmail(String email)  {
        var exist = userRepository.existsUserByEmail(email);

        //----> Check for existence of user.
        if (!exist){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist!");
        }

        //----> Get the user by 1id.
        return   this.userRepository.findUserByEmail(email);
    }


    private void checkForOrderExistence(UUID id){
        var exist = this.userRepository.existsById(id);

        //----> Check for existence of order.
        if (!exist){
            throw new NotFoundException("Order does not exist!");
        }
    }

}

