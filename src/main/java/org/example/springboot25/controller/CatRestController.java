package org.example.springboot25.controller;

import org.example.springboot25.dto.CatInputDTO;
import org.example.springboot25.dto.CatOutputDTO;
import org.example.springboot25.dto.CatUpdateDTO;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.service.CatService;
import org.example.springboot25.service.UserService;
import org.example.springboot25.entities.Cat;
import org.example.springboot25.entities.User;
import org.example.springboot25.entities.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    private boolean isNotOwnerOrAdmin(Cat cat, User currentUser) {
        if (cat.getUserCatOwner() == null) {
            return true; // Om ägare saknas, neka åtkomst
        }
        boolean isOwner = cat.getUserCatOwner().getUserId().equals(currentUser.getUserId());
        boolean isAdmin = currentUser.getUserRole() == UserRole.ADMIN;
        return !(isOwner || isAdmin);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<CatOutputDTO> getAllCats(Authentication auth) {
        User currentUser = userService.getUserByUserName(auth.getName());
        return catService.getAllCatsByOwner(currentUser);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{catId}")
    public CatOutputDTO getCatById(@PathVariable Long catId, Authentication auth) {
        Cat cat = catService.getCatById(catId)
                .orElseThrow(() -> new NotFoundException("Cat not found"));
        User current = userService.getUserByUserName(auth.getName());
        if (isNotOwnerOrAdmin(cat, current)) {
            throw new AccessDeniedException("You can only access your own cats.");
        }
        return catService.toOutputDTO(cat);
    }

    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM')")
    @PostMapping
    public ResponseEntity<CatOutputDTO> createCat(@RequestBody CatInputDTO dto, Authentication auth) {
        User owner = userService.getUserByUserName(auth.getName());
        CatOutputDTO created = catService.createCat(dto, owner);
        return ResponseEntity.created(URI.create("/api/cats/" + created.getCatId())).body(created);
    }

    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM', 'ADMIN')")
    @PutMapping("/{catId}")
    public ResponseEntity<CatOutputDTO> updateCat(@PathVariable Long catId, @RequestBody CatInputDTO dto, Authentication auth) {
        Cat existing = catService.getCatById(catId)
                .orElseThrow(() -> new NotFoundException("Cat not found"));
        User currentUser = userService.getUserByUserName(auth.getName());
        if (isNotOwnerOrAdmin(existing, currentUser)) {
            throw new AccessDeniedException("You can only access your own cats.");
        }
        CatOutputDTO updated = catService.updateCat(catId, dto);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM', 'ADMIN')")
    @PatchMapping("/{catId}")
    public CatOutputDTO partialUpdateCat(@PathVariable Long catId, @RequestBody Map<String, Object> updates, Authentication auth) {
        Cat existing = catService.getCatById(catId)
                .orElseThrow(() -> new NotFoundException("Cat not found"));
        User currentUser = userService.getUserByUserName(auth.getName());
        if (isNotOwnerOrAdmin(existing, currentUser)) {
            throw new AccessDeniedException("You can only update your own cats.");
        }
        return catService.partialUpdateCat(catId, updates);
    }

    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM', 'ADMIN')")
    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> deleteCat(@PathVariable Long catId, Authentication auth) {
        Cat cat = catService.getCatById(catId)
                .orElseThrow(() -> new NotFoundException("Cat not found"));
        User currentUser = userService.getUserByUserName(auth.getName());
        if (isNotOwnerOrAdmin(cat, currentUser)) {
            throw new AccessDeniedException("You can only delete your own cats.");
        }
        catService.deleteCat(catId);
        return ResponseEntity.noContent().build();
    }
}
