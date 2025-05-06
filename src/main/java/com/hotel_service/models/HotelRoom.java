package com.hotel_service.models;

import java.util.List;

import com.hotel_service.models.abstraction.ServiceItem;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/*
    @author Berkeshchuk
    @project hotel-service
    @class HotelRoom
    @version 1.0.0
    @since 5/3/2025-11.28
*/

@Entity
@Table(name = "hotel_room")
@AllArgsConstructor
@NoArgsConstructor
public class HotelRoom extends ServiceItem{

    @Column(nullable = false)
    private int area;
    @Column(nullable = false)
    private int rooms;

    @ElementCollection
    @CollectionTable(name = "room_amenities", joinColumns = @JoinColumn(name="room_id"))
    @Column(name = "amenitie")
    private List<String> amenities;

    @Column(nullable = false)
    private boolean isAvailable;
}
