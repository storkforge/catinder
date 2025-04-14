package org.example.springboot25.mapper;

import org.example.springboot25.dto.UserInputDTO;
import org.example.springboot25.dto.UserOutputDTO;
import org.example.springboot25.dto.UserUpdateDTO;
import org.example.springboot25.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // Konvertera från UserOutputDTO till User
    public User toUser(UserOutputDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("UserOutputDTO cannot be null");
        }
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

    // Konvertera från User till UserUpdateDTO
    public UserUpdateDTO toUserUpdateDTO(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserFullName(user.getUserFullName());
        dto.setUserName(user.getUserName());
        dto.setUserEmail(user.getUserEmail());
        dto.setUserLocation(user.getUserLocation());
        dto.setUserRole(user.getUserRole());
        dto.setUserAuthProvider(user.getUserAuthProvider());
        return dto;
    }
    public User toUser(UserInputDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("UserInputDTO cannot be null");
        }
        if (!isValidEmail(dto.getUserEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }

        User user = new User();
        user.setUserFullName(dto.getUserFullName());
        user.setUserName(dto.getUserName());
        user.setUserEmail(dto.getUserEmail());
        user.setUserLocation(dto.getUserLocation());
        user.setUserRole(dto.getUserRole());
        user.setUserAuthProvider(dto.getUserAuthProvider());
        return user;
    }
    /**
     * Applies partial updates from UserUpdateDTO to a User entity.
     * Performs basic field validation (e.g., email format).
     * This method is used by the service layer to delegate update field logic.
     */
    public void updateUserFromDto(UserUpdateDTO dto, User user) {
        if(dto.getUserEmail() != null &&  !isValidEmail(dto.getUserEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (dto.getUserFullName() != null) {
            user.setUserFullName(dto.getUserFullName()); }
        if (dto.getUserName() != null) {
            user.setUserName(dto.getUserName()); }
        if (dto.getUserEmail() != null) {
            user.setUserEmail(dto.getUserEmail()); }
        if (dto.getUserLocation() != null) {
            user.setUserLocation(dto.getUserLocation()); }
        if (dto.getUserRole() != null) {
            user.setUserRole(dto.getUserRole()); }
        if (dto.getUserAuthProvider() != null) {
            user.setUserAuthProvider(dto.getUserAuthProvider()); }
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

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

}
