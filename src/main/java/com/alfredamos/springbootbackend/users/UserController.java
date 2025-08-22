package com.alfredamos.springbootbackend.users;

import com.alfredamos.springbootbackend.auth.OwnerCheck;
import com.alfredamos.springbootbackend.utils.ResponseMessage;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final OwnerCheck  ownerCheck;

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage> deleteUserById(@PathVariable(name = "id") UUID id){
        //----> Delete the user with the given id.
        var response = userService.deleteUserById(id, ownerCheck.userIdMatchesContextUserId(id));

        //----> Send back the response.
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(name = "id") UUID id){
        //----> Delete the user with the given id.
        var response = userService.getUserById(id, ownerCheck.userIdMatchesContextUserId(id));

        //----> Send back the response.
        return ResponseEntity.ok(response);
    }



}
