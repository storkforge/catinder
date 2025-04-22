package org.example.springboot25.controller;

import org.example.springboot25.dto.CatInputDTO;
import org.example.springboot25.dto.CatOutputDTO;
import org.example.springboot25.dto.CatUpdateDTO;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.mapper.CatMapper;
import org.example.springboot25.service.CatService;
import org.example.springboot25.service.UserService;
import org.example.springboot25.entities.Cat;
import org.example.springboot25.entities.User;
import org.example.springboot25.entities.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/cats")
public class CatRestController {

    private final CatService catService;
    private final UserService userService;
    private final CatMapper catMapper;

    public CatRestController(CatService catService, UserService userService, CatMapper catMapper) {
        this.catService = catService;
        this.userService = userService;
        this.catMapper = catMapper;
    }

    private boolean isNotOwnerOrAdmin(Cat cat, User currentUser) {
        if (cat.getUserCatOwner() == null) {
            return true;
        }
        boolean isOwner = cat.getUserCatOwner().getUserId().equals(currentUser.getUserId());
        boolean isAdmin = currentUser.getUserRole() == UserRole.ADMIN;
        return !(isOwner || isAdmin);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<CatOutputDTO> getAllCats(Authentication auth) {
        User currentUser = userService.findUserByUserName(auth.getName());
        return catService.getAllCatsByUserId(currentUser.getUserId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{catId}")
    public ResponseEntity<CatOutputDTO> getCatById(@PathVariable Long catId, Authentication auth) {
        Cat cat = catService.getCatById(catId); // now throws NotFoundException if not found

        User current = userService.findUserByUserName(auth.getName());
        if (isNotOwnerOrAdmin(cat, current)) {
            throw new AccessDeniedException("You can only access your own cats.");
        }

        return ResponseEntity.ok(catMapper.toDTO(cat));
    }

    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM')")
    @PostMapping
    public ResponseEntity<CatOutputDTO> createCat(@RequestBody CatInputDTO catInputDTO, Authentication auth) {
        User currentUser = userService.findUserByUserName(auth.getName());
        catInputDTO.setUserId(currentUser.getUserId());
        CatOutputDTO createdCat = catService.createCat(catInputDTO);

        return ResponseEntity
                .created(URI.create("/api/cats/" + createdCat.getCatId()))
                .body(createdCat);
    }

    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM', 'ADMIN')")
    @PutMapping("/{catId}")
    public ResponseEntity<CatOutputDTO> updateCat(@PathVariable Long catId, @RequestBody CatInputDTO catInputDTO, Authentication auth) {
        Cat existing = catService.getCatById(catId); // will throw NotFoundException if not found

        User currentUser = userService.findUserByUserName(auth.getName());
        if (isNotOwnerOrAdmin(existing, currentUser)) {
            throw new AccessDeniedException("You can only update your own cats.");
        }

        catInputDTO.setUserId(currentUser.getUserId());
        CatOutputDTO updatedCat = catService.updateCat(catId, catInputDTO);
        return ResponseEntity.ok(updatedCat);
    }

    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM', 'ADMIN')")
    @PatchMapping("/{catId}")
    public ResponseEntity<CatOutputDTO> partialUpdateCat(@PathVariable Long catId, @RequestBody CatUpdateDTO catUpdateDTO, Authentication auth) {
        Cat existing = catService.getCatById(catId); // now throws NotFoundException if not found

        User current = userService.findUserByUserName(auth.getName());
        if (isNotOwnerOrAdmin(existing, current)) {
            throw new AccessDeniedException("You can only update your own cats.");
        }

        CatOutputDTO updated = catService.partialUpdateCat(catId, catUpdateDTO);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM', 'ADMIN')")
    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> deleteCat(@PathVariable Long catId, Authentication auth) {
        Cat cat = catService.getCatById(catId); // no longer needs orElseThrow

        User current = userService.findUserByUserName(auth.getName());
        if (isNotOwnerOrAdmin(cat, current)) {
            throw new AccessDeniedException("You can only delete your own cats.");
        }

        catService.deleteCat(catId);
        return ResponseEntity.noContent().build();
    }
}
