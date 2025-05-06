package com.hotel_service.models;

import com.hotel_service.models.abstraction.ServiceItem;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/*
    @author Berkeshchuk
    @project hotel-service
    @class Transport
    @version 1.0.0
    @since 5/3/2025-11.28
*/

@Entity
@Table(name = "transports")
@AllArgsConstructor
@NoArgsConstructor
public class Transport extends ServiceItem {

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false)
    private double pricePerDay; 



}
