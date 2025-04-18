package org.example.springboot25.controller;


import org.example.springboot25.dto.UserInputDTO;
import org.example.springboot25.dto.UserOutputDTO;
import org.example.springboot25.dto.UserUpdateDTO;
import org.example.springboot25.entities.User;
import org.example.springboot25.exceptions.GlobalViewExceptionHandler;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.mapper.UserMapper;
import org.example.springboot25.service.CatService;
import org.example.springboot25.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserViewController.class)
@WithMockUser
@Import(GlobalViewExceptionHandler.class)
class UserViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private CatService catService;

    @MockitoBean
    private UserMapper userMapper;

    private User mockCurrentUser() {
        User user = new User();
        user.setUserId(1L);
        user.setUserName("testuser");
        return user;
    }

    @Test
    @DisplayName("Should show user list page with users")
    void shouldShowUserList() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(new UserOutputDTO()));

        mockMvc.perform(get("/users").flashAttr("currentUser", mockCurrentUser()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/user-list"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    @DisplayName("Should show error message if no users are found")
    void shouldShowErrorMessageIfNoUsersAreFound() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users").flashAttr("currentUser", mockCurrentUser()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/user-list"))
                .andExpect(model().attributeExists("usersError"));
    }

    @Test
    @DisplayName("Should load add user form")
    void shouldLoadAddUserForm() throws Exception {
        mockMvc.perform(get("/users/add").flashAttr("currentUser", mockCurrentUser())
                        .flashAttr("user", new UserInputDTO()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/user-add"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @DisplayName("Should show user profile by ID")
    void shouldShowUserProfileById() throws Exception {
        UserOutputDTO dto = new UserOutputDTO();
        User user = new User();
        when(userService.getUserDtoById(1L)).thenReturn(dto);
        when(userMapper.toUser(dto)).thenReturn(user);
        when(catService.getAllCatsByUser(user)).thenReturn(List.of());

        mockMvc.perform(get("/users/profile/id/1").flashAttr("currentUser", mockCurrentUser()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/user-details"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("cats"));
    }

    @Test
    @DisplayName("Should show user profile by username")
    void shouldShowUserProfileByUsername() throws Exception {
        when(userService.findUserByUserName("@testuser")).thenReturn(new User());

        mockMvc.perform(get("/users/profile/@testuser").flashAttr("currentUser", mockCurrentUser()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/user-details"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @DisplayName("Should load edit user form")
    void shouldLoadEditUserForm() throws Exception {
        UserOutputDTO dto = new UserOutputDTO();
        when(userService.getUserDtoById(1L)).thenReturn(dto);
        when(userMapper.outputToUpdateDTO(dto)).thenReturn(new UserUpdateDTO());

        mockMvc.perform(get("/users/edit/1").flashAttr("currentUser", mockCurrentUser()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/user-update"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("userId"));
    }

    @Test
    @DisplayName("Should delete user from list")
    void shouldDeleteUserFromList() throws Exception {
        mockMvc.perform(post("/users/delete/1")
                        .with(csrf())
                        .flashAttr("currentUser", mockCurrentUser()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/list?success=deleted"));
    }

    @Test
    @DisplayName("Should return error page on exception during ID profile load")
    void shouldReturnErrorPageOnExceptionDuringIdProfileLoad() throws Exception {
        when(userService.getUserDtoById(999L)).thenThrow(new NotFoundException("User not found"));

        mockMvc.perform(get("/users/profile/id/999")
                        .flashAttr("currentUser", mockCurrentUser())
                        .accept(MediaType.TEXT_HTML)
                        .header("Content-Type", "text/html"))
                .andExpect(status().isOk())
                .andExpect(view().name("error-page"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    @DisplayName("Should return error page on exception during username profile load")
    void shouldReturnErrorPageOnExceptionDuringUserNameProfileLoad() throws Exception {
        when(userService.findUserByUserName("nonexistent")).thenThrow(new NotFoundException("User not found"));

        mockMvc.perform(get("/users/profile/nonexistent").flashAttr("currentUser", mockCurrentUser()))
                .andExpect(status().isOk())
                .andExpect(view().name("error-page"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    @DisplayName("Should handle update user form with errors")
    void shouldHandleUpdateUserFormWithErrors() throws Exception {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserName("");

        BindingResult result = new BeanPropertyBindingResult(dto, "user");
        result.rejectValue("userName", "NotBlank", "Username is required");

        mockMvc.perform(put("/users/1/edit")
                        .with(csrf())
                        .flashAttr("currentUser", mockCurrentUser())
                        .flashAttr("user", dto)
                        .flashAttr("org.springframework.validation.BindingResult.user", result))
                .andExpect(status().isOk())
                .andExpect(view().name("user/user-update"))
                .andExpect(model().attributeExists("userId"))
                .andExpect(model().attributeHasFieldErrors("user", "userName"));
    }

    @Test
    @DisplayName("Should delete own account")
    void shouldDeleteOwnAccount() throws Exception {
        mockMvc.perform(delete("/users/1")
                        .with(csrf())
                        .flashAttr("currentUser", mockCurrentUser()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

}
