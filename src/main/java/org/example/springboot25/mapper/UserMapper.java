package org.example.springboot25.mapper;

import org.example.springboot25.dto.UserInputDTO;
import org.example.springboot25.dto.UserOutputDTO;
import org.example.springboot25.dto.UserUpdateDTO;
import org.example.springboot25.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toUser(UserInputDTO dto) {
        if (dto == null) throw new IllegalArgumentException("UserInputDTO cannot be null");

        User user = new User();
        user.setUserFullName(dto.getUserFullName());
        user.setUserName(dto.getUserName());
        user.setUserEmail(dto.getUserEmail());
        user.setUserLocation(dto.getUserLocation());
        user.setUserRole(dto.getUserRole());
        user.setUserAuthProvider(dto.getUserAuthProvider());
        return user;
    }

    public User toUser(UserOutputDTO dto) {
        if (dto == null) throw new IllegalArgumentException("UserOutputDTO cannot be null");

        User user = new User();
        user.setUserId(dto.getUserId());
        user.setUserFullName(dto.getUserFullName());
        user.setUserName(dto.getUserName());
        user.setUserEmail(dto.getUserEmail());
        user.setUserLocation(dto.getUserLocation());
        user.setUserRole(dto.getUserRole());
        user.setUserAuthProvider(dto.getUserAuthProvider());
        return user;
    }

    public UserOutputDTO toDto(User user) {
        if (user == null) throw new IllegalArgumentException("User cannot be null");

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

    public UserUpdateDTO toUserUpdateDTO(User user) {
        if (user == null) throw new IllegalArgumentException("User cannot be null");

        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserFullName(user.getUserFullName());
        dto.setUserName(user.getUserName());
        dto.setUserEmail(user.getUserEmail());
        dto.setUserLocation(user.getUserLocation());
        dto.setUserRole(user.getUserRole());
        dto.setUserAuthProvider(user.getUserAuthProvider());
        return dto;
    }

    public UserUpdateDTO outputToUpdateDTO(UserOutputDTO dto) {
        if (dto == null) throw new IllegalArgumentException("UserOutputDTO cannot be null");

        UserUpdateDTO updateDTO = new UserUpdateDTO();
        updateDTO.setUserFullName(dto.getUserFullName());
        updateDTO.setUserName(dto.getUserName());
        updateDTO.setUserEmail(dto.getUserEmail());
        updateDTO.setUserLocation(dto.getUserLocation());
        updateDTO.setUserRole(dto.getUserRole());
        updateDTO.setUserAuthProvider(dto.getUserAuthProvider());
        return updateDTO;
    }

    public void updateUserFromDto(UserUpdateDTO dto, User user) {
        if (dto == null || user == null) throw new IllegalArgumentException("DTO or User cannot be null");

        if (dto.getUserEmail() != null && !isValidEmail(dto.getUserEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (dto.getUserFullName() != null) user.setUserFullName(dto.getUserFullName());
        if (dto.getUserName() != null) user.setUserName(dto.getUserName());
        if (dto.getUserEmail() != null) user.setUserEmail(dto.getUserEmail());
        if (dto.getUserLocation() != null) user.setUserLocation(dto.getUserLocation());
        if (dto.getUserRole() != null) user.setUserRole(dto.getUserRole());
        if (dto.getUserAuthProvider() != null) user.setUserAuthProvider(dto.getUserAuthProvider());
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}