package com.hotel_service.mappers_dto;

import com.hotel_service.models_dto.UserDTO;
import com.hotel_service.models.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;


import java.util.List;

/*
    @author Berkeshchuk
    @project hotel-service
    @class UserMapper
    @version 1.0.0
    @since 5/3/2025-12.16
*/

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
//    @Mapping(target = "password", ignore = true)
    UserDTO toDTO(User user);

    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    User toEntity(UserDTO userDTO);
    List<UserDTO> toListDTO(List<User> users);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User updateEntity(UserDTO dto, @MappingTarget User entity);
}
