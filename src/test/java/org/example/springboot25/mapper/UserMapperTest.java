package org.example.springboot25.mapper;

import org.example.springboot25.dto.UserInputDTO;
import org.example.springboot25.dto.UserOutputDTO;
import org.example.springboot25.dto.UserUpdateDTO;
import org.example.springboot25.entities.User;
import org.example.springboot25.entities.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
    }

    @Test
    @DisplayName("Should map UserInput to User")
    void shouldMapUserInputToUser() {
        UserInputDTO userInputDTO = new UserInputDTO("Name", "@user",
                "mail@test.com", "City", UserRole.BASIC, "LOCAL");
        User user = userMapper.toUser(userInputDTO);

        assertEquals(userInputDTO.getUserName(), user.getUserName());
        assertEquals(userInputDTO.getUserEmail(), user.getUserEmail());
    }

    @Test
    @DisplayName("Should map UserOutputDTO to User")
    void shouldMapUserOutputDTOToUser() {
        UserOutputDTO outputDTO = new UserOutputDTO(1L, "Name",
                "@user", "mail@test.com", "City", UserRole.BASIC, "LOCAL");
        User user = userMapper.toUser(outputDTO);

        assertEquals(outputDTO.getUserName(), user.getUserName());
        assertEquals(outputDTO.getUserEmail(), user.getUserEmail());
    }

    @Test
    @DisplayName("Should map User to UserOutputDTO")
    void shouldMapUserToUserOutputDTO() {
        User user = new User();
        user.setUserId(1L);
        user.setUserName("Name");
        user.setUserName("@user");
        user.setUserEmail("mail@test.com");
        user.setUserRole(UserRole.BASIC);
        user.setUserLocation("City");


        UserOutputDTO userOutputDTO = userMapper.toDto(user);

        assertEquals(user.getUserName(), userOutputDTO.getUserName());
        assertEquals(user.getUserEmail(), userOutputDTO.getUserEmail());
    }

    @Test
    @DisplayName("Should map User to UserUpdateDTO")
    void shouldMapUserToUserUpdateDTO() {
        User user = new User();
        user.setUserName("@user");
        user.setUserRole(UserRole.BASIC);

        UserUpdateDTO userUpdateDTO = userMapper.toUserUpdateDTO(user);
        assertEquals(user.getUserName(), userUpdateDTO.getUserName());
        assertEquals(user.getUserRole(), userUpdateDTO.getUserRole());
    }

    @Test
    @DisplayName("Should map OutputDTO to UpdateDTO")
    void shouldMapOutputDTOToUpdateDTO() {
        UserOutputDTO userOutputDTO = new UserOutputDTO(1L, "Name", "@user", "email@test.com", "City", UserRole.ADMIN, "LOCAL");
        UserUpdateDTO userUpdateDTO = userMapper.outputToUpdateDTO(userOutputDTO);

        assertEquals(userOutputDTO.getUserName(), userUpdateDTO.getUserName());
        assertEquals(userOutputDTO.getUserEmail(), userUpdateDTO.getUserEmail());
    }

    @Test
    @DisplayName("Should update USer from UpdateDTO")
    void shouldUpdateUserFromUpdateDTO() {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO("New Name",
                "@newuser", "new@test.com", "NewCity", UserRole.ADMIN, "GOOGLE");
        User user = new User();

        userMapper.updateUserFromDto(userUpdateDTO, user);

        assertEquals("New Name", user.getUserFullName());
        assertEquals("@newuser", user.getUserName());
        assertEquals("new@test.com", user.getUserEmail());
        assertEquals("NewCity", user.getUserLocation());
        assertEquals(UserRole.ADMIN, user.getUserRole());
        assertEquals("GOOGLE", user.getUserAuthProvider());

    }

    @Test
    @DisplayName("Should throw IllegalArgumentException if UserInputDTO is null")
    void shouldThrowIllegalArgumentExceptionIfUserInputDTOIsNull() {
        assertThrows(IllegalArgumentException.class, () -> userMapper.toUser((UserInputDTO) null));

    }

    @Test
    @DisplayName("Should throw IllegalArgumentException if UserOutputDTO is null")
    void shouldThrowIllegalArgumentExceptionIfUserOutputDTOIsNull() {
        assertThrows(IllegalArgumentException.class, () -> userMapper.outputToUpdateDTO(null));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException if User is null for toDto")
    void shouldThrowIllegalArgumentExceptionIfUserIsNullForToDto() {
        assertThrows(IllegalArgumentException.class, () -> userMapper.toDto(null));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException if null passed to updateUserFromDto")
    void shouldThrowIllegalArgumentExceptionIfNullPassedToUpdateUserFromDto() {
        assertThrows(IllegalArgumentException.class, () -> userMapper.updateUserFromDto(null, new User()));
        assertThrows(IllegalArgumentException.class, () -> userMapper.updateUserFromDto(new UserUpdateDTO(), null));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for invalid mail in updateUserFromDto")
    void shouldThrowIllegalArgumentExceptionForInvalidMailInUpdateUserFromDto() {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
        userUpdateDTO.setUserEmail("invalidmail");
        User user = new User();

        assertThrows(IllegalArgumentException.class, () -> userMapper.updateUserFromDto(userUpdateDTO, user));
    }
}
