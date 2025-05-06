package com.hotel_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel_service.models.Spa;

/*
    @author Berkeshchuk
    @project hotel-service
    @class SpaRepository
    @version 1.0.0
    @since 5/3/2025-11.28
*/

public interface SpaRepository extends JpaRepository<Spa, Integer> {
    
}
