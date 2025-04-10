package org.example.springboot25.Mapper;

import org.example.springboot25.DTO.UserInputDTO;
import org.example.springboot25.DTO.UserOutputDTO;
import org.example.springboot25.DTO.UserUpdateDTO;
import org.example.springboot25.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toUser(UserInputDTO dto) {
        User user = new User();
        user.setUserFullName(dto.getUserFullName());
        user.setUserName(dto.getUserName());
        user.setUserEmail(dto.getUserEmail());
        user.setUserLocation(dto.getUserLocation());
        user.setUserRole(dto.getUserRole());
        user.setUserAuthProvider(dto.getUserAuthProvider());
        return user;
    }

    public void updateUserFromDto(UserUpdateDTO dto, User user) {
        if (dto.getUserFullName() != null) user.setUserFullName(dto.getUserFullName());
        if (dto.getUserName() != null) user.setUserName(dto.getUserName());
        if (dto.getUserEmail() != null) user.setUserEmail(dto.getUserEmail());
        if (dto.getUserLocation() != null) user.setUserLocation(dto.getUserLocation());
        if (dto.getUserRole() != null) user.setUserRole(dto.getUserRole());
        if (dto.getUserAuthProvider() != null) user.setUserAuthProvider(dto.getUserAuthProvider());
    }

    public UserOutputDTO toDto(User user) {
        UserOutputDTO dto = new UserOutputDTO();
        dto.setUserId(user.getUserId());
        dto.setUserFullName(user.getUserFullName());
        dto.setUserName(user.getUserName());
        dto.setUserEmail(user.getUserEmail());
        dto.setUserLocation(user.getUserLocation());
        dto.setUserRole(user.getUserRole());
        dto.setUserAuthProvider(user.getUserAuthProvider());
        return dto;
    }
}
