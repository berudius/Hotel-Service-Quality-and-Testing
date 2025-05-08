package com.hotel_service.models;

import com.hotel_service.stat.Role;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/*
    @author Berkeshchuk
    @project hotel-service
    @class User
    @version 1.0.0
    @since 5/3/2025-11.28
*/

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String mobileNumber;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "role", nullable = false)

    @Setter(value = AccessLevel.NONE)
    private Role role;

    @Column(updatable = false)
//    @Setter (AccessLevel.NONE)
    private LocalDateTime createdOn;
//    = LocalDateTime.now();

    @Column
//    @Setter (AccessLevel.NONE)
    private LocalDateTime updatedOn;


//    @PrePersist
//    private void setCreatedOnValue(){
//        createdOn = LocalDateTime.now();
//    }
//
//    @PreUpdate
//    private void setUpdatedon(){
//        updatedOn = LocalDateTime.now();
//    }


    public void setRole(String role){
        this.role = Role.valueOf(role.toUpperCase());
    }

    public User(String name, String email, String password, String mobileNumber, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobileNumber = mobileNumber;
        this.role = role;
    }

}
