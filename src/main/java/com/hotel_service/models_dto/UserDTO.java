package com.hotel_service.models_dto;

import com.hotel_service.stat.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/*
    @author Berkeshchuk
    @project hotel-service
    @class UserDTO
    @version 1.0.0
    @since 5/3/2025-11.52
*/

@AllArgsConstructor
@Data
@Builder
public class UserDTO {
    private int id;
    private String name;
    private String email;
    private String password;
    private String mobileNumber;
    private Role role;
}


