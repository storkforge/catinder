//package org.example.springboot25.controller;
//
//import org.example.springboot25.config.UserTestMockConfig;
//import org.example.springboot25.dto.UserInputDTO;
//import org.example.springboot25.dto.UserOutputDTO;
//import org.example.springboot25.dto.UserUpdateDTO;
//import org.example.springboot25.entities.User;
//import org.example.springboot25.entities.UserRole;
//import org.example.springboot25.service.UserService;
//import org.junit.jupiter.api.*;
//
//import org.mockito.Mockito;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.http.MediaType;
//
//import static org.mockito.ArgumentMatchers.eq;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//
//
//import static org.mockito.ArgumentMatchers.any;
//
//import java.util.List;
//
//@Import(UserTestMockConfig.class)
//@WebMvcTest(controllers = UserRestController.class)
//class UserRestControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private UserService userService;
//
//    private final UserOutputDTO testUserDto = new UserOutputDTO(1L,
//            "Test User", "@testuser", "test@example.com",
//            "TestCity", UserRole.BASIC, "google");
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    @DisplayName("Should return all users when called by admin")
//    void shouldReturnAllUsersWhenCalledByAdmin() throws Exception {
//        Mockito.when(userService.getAllUsers()).thenReturn(List.of(testUserDto));
//
//        mockMvc.perform(get("/api/users"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].userName").value("@testuser"));
//    }
//
//    @Test
//    @WithMockUser(username = "@testuser", roles = "BASIC")
//    @DisplayName("Should return user by ID if requester is owner")
//    void shouldReturnUserByIdIfRequesterIsOwner() throws Exception {
//        User testUser = new User();
//        testUser.setUserId(1L);
//        testUser.setUserName("@testuser");
//        testUser.setUserRole(UserRole.BASIC);
//        testUser.setUserEmail("@testuser@example.com");
//
//        Mockito.when(userService.findUserByUserName("@testuser")).thenReturn(testUser);
//        Mockito.when(userService.getUserDtoById(1L)).thenReturn(testUserDto);
//
//        mockMvc.perform(get("/api/users/id/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.userEmail").value("test@example.com"));
//    }
//
//    @Test
//    @WithMockUser(username = "user", roles = "ADMIN")
//    @DisplayName("Should return user by ID if requester is admin")
//    void shouldReturnUserByIdIfRequesterIsAdmin() throws Exception {
//        User adminUser = new User();
//        adminUser.setUserId(99L);
//        adminUser.setUserName("user");
//        adminUser.setUserRole(UserRole.ADMIN);
//        adminUser.setUserEmail("admin@example.com");
//
//        Mockito.when(userService.findUserByUserName("user")).thenReturn(adminUser);
//        Mockito.when(userService.getUserDtoById(1L)).thenReturn(testUserDto);
//
//        mockMvc.perform(get("/api/users/id/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.userFullName").value("Test User"));
//    }
//
//    @Test
//    @DisplayName("Should create user successfully")
//    @WithMockUser(username = "@testuser")
//    void shouldCreateUserSuccessfully() throws Exception {
//        Mockito.when(userService.addUser(any(UserInputDTO.class))).thenReturn(testUserDto);
//
//        mockMvc.perform(post("/api/users")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("""
//                        {
//                          "userFullName": "Test User",
//                          "userName": "@testuser",
//                          "userEmail": "test@example.com",
//                          "userLocation": "TestCity",
//                          "userRole": "BASIC",
//                          "userAuthProvider": "google"
//                        }
//                        """))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.userId").value(1));
//    }
//
//    @Test
//    @WithMockUser(username = "@testuser", roles = "BASIC")
//    @DisplayName("Should update user when requester is owner")
//    void shouldUpdateUserWhenRequesterIsOwner() throws Exception {
//        UserUpdateDTO testUpdateDto = new UserUpdateDTO(
//                "Updated User", "@testuser", "updated@example.com",
//                "UpdatedCity", UserRole.BASIC, "google"
//        );
//
//        UserOutputDTO updatedUserDto = new UserOutputDTO(
//                1L, "Updated User", "@testuser", "updated@example.com",
//                "UpdatedCity", UserRole.BASIC, "google"
//        );
//
//        User testUser = new User();
//        testUser.setUserId(1L);
//        testUser.setUserName("@testuser");
//        testUser.setUserRole(UserRole.BASIC);
//        testUser.setUserEmail("@testuser@example.com");
//
//        Mockito.when(userService.findUserByUserName("@testuser")).thenReturn(testUser);
//        Mockito.when(userService.getUserDtoById(1L)).thenReturn(testUserDto);
//        Mockito.when(userService.updateUser(eq(1L), any(UserUpdateDTO.class))).thenReturn(updatedUserDto);
//
//        mockMvc.perform(put("/api/users/1")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("""
//                            {
//                              "userFullName": "Updated User",
//                              "userName": "@testuser",
//                              "userEmail": "updated@example.com",
//                              "userLocation": "UpdatedCity",
//                              "userRole": "BASIC",
//                              "userAuthProvider": "google"
//                            }
//                            """))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.userLocation").value("UpdatedCity"));
//    }
//    @Test
//    @WithMockUser(username = "@testuser", roles = "BASIC")
//    @DisplayName("Should delete user when requester is owner")
//    void shouldDeleteUserWhenRequesterIsOwner() throws Exception {
//    User testUser = new User();
//    testUser.setUserId(1L);
//    testUser.setUserName("@testuser");
//    testUser.setUserRole(UserRole.BASIC);
//    testUser.setUserEmail("@testuser@example.com");
//
//    Mockito.when(userService.findUserByUserName("@testuser")).thenReturn(testUser);
//    Mockito.when(userService.getUserDtoById(1L)).thenReturn(testUserDto);
//
//    mockMvc.perform(delete("/api/users/1")
//                    .with(csrf())) //
//            .andExpect(status().isNoContent());
//}
//
//    @Test
//    @WithMockUser
//    @DisplayName("Should return user by username")
//    void shouldReturnUserByUsername() throws Exception {
//        Mockito.when(userService.getUserDtoByUserName("@testuser")).thenReturn(testUserDto);
//
//        mockMvc.perform(get("/api/users/username/@testuser"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.userFullName").value("Test User"));
//    }
//
//    @Test
//    @WithMockUser
//    @DisplayName("Should return users by location")
//    void shouldReturnUsersByLocation() throws Exception {
//        Mockito.when(userService.getAllUsersByLocation("TestCity")).thenReturn(List.of(testUserDto));
//
//        mockMvc.perform(get("/api/users/by-location/TestCity"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].userName").value("@testuser"));
//    }
//
//    @Test
//    @WithMockUser
//    @DisplayName("Should return users by role")
//    void shouldReturnUsersByRole() throws Exception {
//        Mockito.when(userService.getAllUsersByRole("BASIC")).thenReturn(List.of(testUserDto));
//
//        mockMvc.perform(get("/api/users/by-role/BASIC"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].userEmail").value("test@example.com"));
//    }
//
//    @Test
//    @WithMockUser
//    @DisplayName("Should search users by username or cat name")
//    void shouldSearchUsersByUsernameOrCatName() throws Exception {
//        Mockito.when(userService.getAllUsersByUserNameOrCatName("test"))
//                .thenReturn(List.of(testUserDto));
//
//        mockMvc.perform(get("/api/users/by-search-term/test"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].userRole").value("BASIC"));
//    }
//}
