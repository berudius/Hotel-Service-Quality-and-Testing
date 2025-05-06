package com.hotel_service.services;

import org.springframework.stereotype.Service;

import com.hotel_service.repository.DishRepository;

import lombok.AllArgsConstructor;

/*
    @author Berkeshchuk
    @project hotel-service
    @class DishService
    @version 1.0.0
    @since 5/3/2025-11.28
*/

@Service
@AllArgsConstructor
public class DishService {
    DishRepository dishRepository;
}
