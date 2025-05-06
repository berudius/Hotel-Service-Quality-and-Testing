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
    @class Spa
    @version 1.0.0
    @since 5/3/2025-11.28
*/

@Entity
@Table(name = "spas")
@AllArgsConstructor
@NoArgsConstructor
public class Spa extends ServiceItem {
    @ElementCollection
    @CollectionTable(name = "spas_amenities", joinColumns = @JoinColumn(name = "spa_id"))
    @Column(name = "amenitie")
    private List<String> amenities;

    @Column(nullable = false)
    private boolean isAvailable;
}
