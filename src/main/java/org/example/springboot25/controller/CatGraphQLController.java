package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.dto.CatInputDTO;
import org.example.springboot25.dto.CatOutputDTO;
import org.example.springboot25.dto.CatUpdateDTO;
import org.example.springboot25.entities.User;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.service.CatService;
import org.example.springboot25.service.UserService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Controller
@Validated
public class CatGraphQLController {

    private final CatService catService;
    private final UserService userService;

    public CatGraphQLController(CatService catService, UserService userService) {
        this.catService = catService;
        this.userService = userService;
    }

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    public List<CatOutputDTO> getAllCats(Authentication auth) {
        User currentUser = userService.findUserByUserName(auth.getName());
        return catService.getAllCatsByUser(currentUser);
    }

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    public CatOutputDTO getCatById(@Argument Long catId, Authentication auth) {
        CatOutputDTO cat = catService.getCatDtoById(catId);
        User currentUser = userService.findUserByUserName(auth.getName());

        if (!cat.getUserId().equals(currentUser.getUserId()) &&
                !currentUser.getUserRole().name().equals("ADMIN")) {
            throw new AccessDeniedException("You can only view your own cats.");
        }

        return cat;
    }

    @MutationMapping
    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM')")
    public CatOutputDTO createCat(@Argument("input") @Valid CatInputDTO input, Authentication auth) {
        User currentUser = userService.findUserByUserName(auth.getName());
        input.setUserId(currentUser.getUserId());
        return catService.addCat(input);
    }

    @MutationMapping
    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM', 'ADMIN')")
    public CatOutputDTO updateCat(@Argument Long catId, @Argument("input") @Valid CatUpdateDTO input, Authentication auth) {
        CatOutputDTO cat = catService.getCatDtoById(catId);
        User currentUser = userService.findUserByUserName(auth.getName());

        if (!cat.getUserId().equals(currentUser.getUserId()) &&
                !currentUser.getUserRole().name().equals("ADMIN")) {
            throw new AccessDeniedException("You can only update your own cats.");
        }

        return catService.updateCat(catId, input);
    }

    @MutationMapping
    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM', 'ADMIN')")
    public boolean deleteCat(@Argument Long catId, Authentication auth) {
        try {
            CatOutputDTO cat = catService.getCatDtoById(catId);
            User currentUser = userService.findUserByUserName(auth.getName());

            if (!cat.getUserId().equals(currentUser.getUserId()) &&
                    !currentUser.getUserRole().name().equals("ADMIN")) {
                throw new AccessDeniedException("You can only delete your own cats.");
            }

            catService.deleteCat(catId);
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }
}
