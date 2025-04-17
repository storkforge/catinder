package org.example.springboot25.controller;

import org.example.springboot25.dto.CatInputDTO;
import org.example.springboot25.dto.CatOutputDTO;
import org.example.springboot25.dto.CatUpdateDTO;
import org.example.springboot25.entities.User;
import org.example.springboot25.service.CatService;
import org.example.springboot25.service.UserService;
import org.springframework.http.ResponseEntity;
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
    public List<Cat> getAllCats(Authentication auth) {
        User currentUser = userService.findUserByUserName(auth.getName());
        return catService.getAllCatsByUser(currentUser);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{catId}")
    public ResponseEntity<Cat> getCatById(@PathVariable Long catId, Authentication auth) {
        Cat cat = catService.getCatById(catId)
                .orElseThrow(() -> new NotFoundException("Cat not found"));

        User current = userService.findUserByUserName(auth.getName());
        if (isNotOwnerOrAdmin(cat, current)) {
            throw new AccessDeniedException("You can only access your own cats.");
        }

        return ResponseEntity.ok(cat);
    }

    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM')")
    @PostMapping
    public ResponseEntity<Cat> createCat(@RequestBody Cat cat, Authentication auth) {
        User currentUser = userService.findUserByUserName(auth.getName());
        cat.setUser(currentUser);
        Cat createdCat = catService.createCat(cat);

        return ResponseEntity
                .created(URI.create("/api/cats/" + createdCat.getCatId()))
                .body(createdCat);
    }

    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM', 'ADMIN')")
    @PutMapping("/{catId}")
    public ResponseEntity updateCat(@PathVariable Long catId, @RequestBody Cat catDetails, Authentication auth) {
        Cat existing = catService.getCatById(catId)
                .orElseThrow(() -> new NotFoundException("Cat not found"));

        User currentUser = userService.findUserByUserName(auth.getName());
        if (isNotOwnerOrAdmin(catDetails, currentUser)) {
            throw new AccessDeniedException("You can only access your own cats.");
        }
        Cat updatedCat = catService.updateCat(catId, catDetails);
        return ResponseEntity.ok(updatedCat);
    }

    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM', 'ADMIN')")
    @PatchMapping("/{catId}")
    public ResponseEntity<Cat> partialUpdateCat(@PathVariable Long catId, @RequestBody Map<String, Object> updates, Authentication auth) {
        Cat existing = catService.getCatById(catId)
                .orElseThrow(() -> new NotFoundException("Cat not found"));

        User current = userService.findUserByUserName(auth.getName());
        if (isNotOwnerOrAdmin(existing, current)) {
            throw new AccessDeniedException("You can only update your own cats.");
        }

        Cat updatedCat = catService.partialUpdateCat(catId, updates);
        return ResponseEntity.ok(updatedCat);
    }

    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM', 'ADMIN')")
    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> deleteCat(@PathVariable Long catId, Authentication auth) {
        Cat cat = catService.getCatById(catId)
                .orElseThrow(() -> new NotFoundException("Cat not found"));

        User current = userService.findUserByUserName(auth.getName());
        if (isNotOwnerOrAdmin(cat, current)) {
            throw new AccessDeniedException("You can only delete your own cats.");
        }

        catService.deleteCat(catId);
        return ResponseEntity.noContent().build();
    }
}
