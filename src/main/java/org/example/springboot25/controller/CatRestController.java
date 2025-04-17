package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.dto.CatInputDTO;
import org.example.springboot25.dto.CatOutputDTO;
import org.example.springboot25.dto.CatUpdateDTO;
import org.example.springboot25.entities.Cat;
import org.example.springboot25.entities.User;
import org.example.springboot25.entities.UserRole;
import org.example.springboot25.service.CatService;
import org.example.springboot25.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cats")
public class CatRestController {

    private final CatService catService;
    private final UserService userService;

    public CatRestController(CatService catService, UserService userService) {
        this.catService = catService;
        this.userService = userService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<CatOutputDTO> getAllCats(Authentication auth) {
        User currentUser = userService.findUserByUserName(auth.getName());
        return catService.getAllCatsByUser(currentUser);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{catId}")
    public ResponseEntity<CatOutputDTO> getCatById(@PathVariable Long catId, Authentication auth) {
        User currentUser = userService.findUserByUserName(auth.getName());
        CatOutputDTO catDTO = catService.getCatDtoById(catId);

        User current = userService.findUserByUserName(auth.getName());
        if (isNotOwnerOrAdmin(cat, current)) {
        if (!catDTO.getUserId().equals(currentUser.getUserId()) && currentUser.getUserRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("You can only access your own cats.");
        }

        return ResponseEntity.ok(catDTO);
    }

    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM')")
    @PostMapping
    public ResponseEntity<CatOutputDTO> createCat(@RequestBody @Valid CatInputDTO inputDTO, Authentication auth) {
        User currentUser = userService.findUserByUserName(auth.getName());
        inputDTO.setUserId(currentUser.getUserId()); // Tilldela ägaren
    public ResponseEntity<Cat> createCat(@RequestBody Cat cat, Authentication auth) {
        User currentUser = userService.findUserByUserName(auth.getName());
        cat.setUser(currentUser);
        Cat createdCat = catService.createCat(cat);

        CatOutputDTO createdCat = catService.addCat(inputDTO);
        return ResponseEntity.created(URI.create("/api/cats/" + createdCat.getCatId())).body(createdCat);
    }

    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM', 'ADMIN')")
    @PutMapping("/{catId}")
    public ResponseEntity<CatOutputDTO> updateCat(@PathVariable Long catId,
                                                  @RequestBody @Valid CatUpdateDTO updateDTO,
                                                  Authentication auth) {
        User currentUser = userService.findUserByUserName(auth.getName());
        CatOutputDTO existingCat = catService.getCatDtoById(catId);

        if (!existingCat.getUserId().equals(currentUser.getUserId()) && currentUser.getUserRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("You can only update your own cats.");
        }

        return ResponseEntity.ok(catService.updateCat(catId, updateDTO));
    }

    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM', 'ADMIN')")
    @PatchMapping("/{catId}")
    public ResponseEntity<CatOutputDTO> partialUpdateCat(@PathVariable Long catId,
                                                         @RequestBody Map<String, Object> updates,
                                                         Authentication auth) {
        User currentUser = userService.findUserByUserName(auth.getName());
        CatOutputDTO existingCat = catService.getCatDtoById(catId);

        if (!existingCat.getUserId().equals(currentUser.getUserId()) && currentUser.getUserRole() != UserRole.ADMIN) {
        User current = userService.findUserByUserName(auth.getName());
        if (isNotOwnerOrAdmin(existing, current)) {
            throw new AccessDeniedException("You can only update your own cats.");
        }

        CatOutputDTO updatedCat = catService.partialUpdateCat(catId, updates);
        return ResponseEntity.ok(updatedCat);
    }

    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM', 'ADMIN')")
    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> deleteCat(@PathVariable Long catId, Authentication auth) {
        User currentUser = userService.findUserByUserName(auth.getName());
        CatOutputDTO catDTO = catService.getCatDtoById(catId);

        if (!catDTO.getUserId().equals(currentUser.getUserId()) && currentUser.getUserRole() != UserRole.ADMIN) {
        User current = userService.findUserByUserName(auth.getName());
        if (isNotOwnerOrAdmin(cat, current)) {
            throw new AccessDeniedException("You can only delete your own cats.");
        }

        catService.deleteCat(catId);
        return ResponseEntity.noContent().build();
    }
}
