package com.hotel_service.models.abstraction;

import java.util.ArrayList;
import java.util.List;
import com.hotel_service.models.ServiceImage;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;

/*
    @author Berkeshchuk
    @project hotel-service
    @class ServiceItem
    @version 1.0.0
    @since 5/3/2025-11.28
*/

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "spa_type")
public abstract class ServiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    
    @OneToMany(mappedBy = "serviceItem", fetch = FetchType.LAZY)
    private List<ServiceImage> image = new ArrayList<>();

    @Column(nullable = false, length = 60)
    private String name;
    @Column(nullable = false, length = 3000)
    private String description;
    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private boolean isAvailable;

    

    public double calculateFinalPrice(){
        return this.price;
    }

 
} 
