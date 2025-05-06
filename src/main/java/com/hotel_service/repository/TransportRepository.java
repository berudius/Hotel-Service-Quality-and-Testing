package com.hotel_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel_service.models.Transport;

/*
    @author Berkeshchuk
    @project hotel-service
    @class TransportRepository
    @version 1.0.0
    @since 5/3/2025-11.28
*/

public interface TransportRepository extends JpaRepository<Transport, Integer> {
    
}
