//package org.example.springboot25;
//
//import org.example.springboot25.config.SecurityConfig;
//import org.example.springboot25.config.UserTestMockConfig;
//import org.example.springboot25.controller.UserRestController;
//import org.example.springboot25.dto.UserOutputDTO;
//import org.example.springboot25.entities.User;
//import org.example.springboot25.entities.UserRole;
//import org.example.springboot25.security.CustomOAuth2UserService;
//import org.example.springboot25.service.UserService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.hamcrest.Matchers.containsString;
//
//@WebMvcTest(UserRestController.class)
//@Import({UserTestMockConfig.class, SecurityConfig.class})
//public class UserSecurityTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private CustomOAuth2UserService customOAuth2UserService;
//
//    @Test
//    @DisplayName("Should redirect unauthenticated user to login OAuth")
//    void shouldRedirectUnauthenticatedUserToLoginOAuth() throws Exception {
//        mockMvc.perform(get("/api/users"))
//                .andExpect(status().isFound()) // 302
//                .andExpect(header().string("Location", containsString("/oauth2/authorization/google")));
//    }
//
//    @Test
//    @WithMockUser(roles = "BASIC")
//    @DisplayName("Should forbid BASIC role from deleting a user")
//    void shouldForbidBASICRoleFromDeletingAUser() throws Exception {
//        Mockito.when(userService.getUserDtoById(1L)).thenReturn(
//                new UserOutputDTO(1L, "Test User", "@testuser",
//                        "test@example.com", "TestCity", UserRole.BASIC, "google"));
//
//        mockMvc.perform(delete("/api/users/1").with(csrf()))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithMockUser(username = "admin", roles = "ADMIN")
//    @DisplayName("Should allow ADMIN to delete a user")
//    void shouldAllowAdminToDeleteUser() throws Exception {
//        String username = "admin";
//        Mockito.when(userService.findUserByUserName(username)).thenReturn(
//                new User(2L, "Admin User", username, "admin@example.com",
//                        "TestCity", UserRole.ADMIN, "google"));
//
//        Mockito.when(userService.getUserDtoById(1L)).thenReturn(
//                new UserOutputDTO(1L, "Regular User", "regular",
//                        "regular@example.com", "City", UserRole.BASIC, "google"));
//
//        mockMvc.perform(delete("/api/users/1").with(csrf()))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    @WithMockUser(username = "@testuser", roles = "BASIC")
//    @DisplayName("Should allow BASIC user to get their own data")
//    void shouldAllowBASICUserToGetOwnData() throws Exception {
//        Mockito.when(userService.findUserByUserName("@testuser")).thenReturn(
//                new User(1L, "Test User", "@testuser",
//                        "test@example.com", "TestCity", UserRole.BASIC, "google"));
//
//        Mockito.when(userService.getUserDtoById(1L)).thenReturn(
//                new UserOutputDTO(1L, "Test User",
//                        "@testuser", "test@example.com", "TestCity", UserRole.BASIC, "google"));
//
//        mockMvc.perform(get("/api/users/id/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.userEmail").value("test@example.com"));
//    }
//
//    @Test
//    @DisplayName("Should redirect unauthenticated user to OAuth2 login when accessing protected API")
//    void shouldRedirectUnauthenticatedUserToOAuthLogin() throws Exception {
//        mockMvc.perform(get("/api/users"))
//                .andExpect(status().isFound())
//                .andExpect(header().string("Location", containsString("/oauth2/authorization/google")));
//    }
//
//    @Test
//    @WithMockUser(username = "user", roles = "BASIC")
//    @DisplayName("Should return 403 if trying to update another user")
//    void shouldReturn403IfNotOwnerOrAdmin() throws Exception {
//        Mockito.when(userService.getUserDtoById(2L)).thenReturn(
//                new UserOutputDTO(2L, "Other", "otheruser",
//                        "other@example.com", "OtherCity", UserRole.BASIC, "google"));
//
//        Mockito.when(userService.findUserByUserName("user")).thenReturn(
//                new User(1L, "Test", "user",
//                        "user@example.com", "Ort", UserRole.BASIC, "google"));
//
//        mockMvc.perform(delete("/api/users/2").with(csrf()))
//                .andExpect(status().isForbidden());
//    }
//
//}
