package com.hotel_service.controllers;

import com.hotel_service.models_dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotel_service.services.UserService;

import lombok.AllArgsConstructor;

/*
    @author Berkeshchuk
    @project hotel-service
    @class UserController
    @version 1.0.0
    @since 5/3/2025-11.28
*/

@RestController
@AllArgsConstructor
public class UserController {

//    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private UserService userService;


    @GetMapping("users/{id}")
    public ResponseEntity<?> getUser(@PathVariable(name = "id") int id) {
        return userService.getUser(id);
    }

    @GetMapping("users")
    public ResponseEntity<?> getUsers(){
//        logger.info("Executing getUsers");
        return userService.getUsers();
    }

    @PostMapping("users/add_user")
    public ResponseEntity<?> addUser(@RequestBody UserDTO user){
       return userService.addUser(user);
    }

    @PutMapping("users/update_user")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO user){
        return userService.updateUser(user);
    }

    @DeleteMapping("users/delete_user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable(name = "id") int id){
        return userService.deleteUser(id);
    }



}
