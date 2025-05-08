package com.hotel_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel_service.models.User;

/*
    @author Berkeshchuk
    @project hotel-service
    @class UserRepository
    @version 1.0.0
    @since 5/3/2025-11.28
*/

public interface UserRepository extends JpaRepository<User, Integer> {
    public boolean existsByEmail(String email);

    public default User findByEmail(String email) {
        return new User();
    }
}
