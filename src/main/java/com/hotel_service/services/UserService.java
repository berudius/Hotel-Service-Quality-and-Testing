package com.hotel_service.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.hotel_service.mappers_dto.UserMapper;
import com.hotel_service.models_dto.UserDTO;
import org.hibernate.PropertyValueException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hotel_service.models.User;
import com.hotel_service.repository.UserRepository;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

/*
    @author Berkeshchuk
    @project hotel-service
    @class UserService
    @version 1.0.0
    @since 5/3/2025-11.28
*/

@Service
@AllArgsConstructor
public class UserService {
    UserRepository userRepository;

    public ResponseEntity<?> getUser(int id){
        try{
           Optional<User> user = userRepository.findById(id);
           if(user.isPresent()){
               return ResponseEntity.ok(user.get());
           }
           return ResponseEntity.badRequest().body("User not found with id " + id);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    public ResponseEntity<?> getUsers(){
        try{
            List <User> users = new ArrayList<>(userRepository.findAll());
            if(users.size() > 0){
                return ResponseEntity.ok(users);
            }
            return ResponseEntity.badRequest().body("No users found");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    public ResponseEntity<?> addUser(UserDTO userDTO){
        try {
            if(userRepository.existsByEmail(userDTO.getEmail())){
                return ResponseEntity.badRequest().body("Email already in use");
            }

            User user = UserMapper.INSTANCE.toEntity(userDTO);
            user.setCreatedOn(LocalDateTime.now());
            int id = userRepository.save(user).getId();
            return ResponseEntity.ok(id);
        }
        catch (Exception e){
            e.printStackTrace();
            return  ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    public ResponseEntity<?> updateUser(UserDTO userDTO){
        try{
            Optional<User> existedUser = userRepository.findById(userDTO.getId());

            if(existedUser.isPresent()){
                User updatedUser = UserMapper.INSTANCE.updateEntity(userDTO, existedUser.get());
                updatedUser.setUpdatedOn(LocalDateTime.now());
                userRepository.save(updatedUser);
                return ResponseEntity.ok(updatedUser);
            }

            return ResponseEntity.badRequest().body("User not found with id " + userDTO.getId());
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    public ResponseEntity<?> deleteUser(int id){
        try{
            Optional<User> existedUser = userRepository.findById(id);
            if(existedUser.isPresent()){
                userRepository.deleteById(id);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.badRequest().body("User not found with id " + id);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
