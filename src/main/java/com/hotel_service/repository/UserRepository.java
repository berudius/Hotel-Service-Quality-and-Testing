package com.hotel_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel_service.models.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/*
    @author Berkeshchuk
    @project hotel-service
    @class UserRepository
    @version 1.0.0
    @since 5/3/2025-11.28
*/

public interface UserRepository extends JpaRepository<User, Integer> {
    public boolean existsByEmail(String email);

    public  User findByEmail(String email);
    List<User> findAllByOrderByNameAsc();

    List<User> findByNameContaining(String namePart);

    List<User> findByCreatedOnBetween(LocalDateTime createdOn, LocalDateTime createdOn2);

     boolean existsByName(String name);
}
