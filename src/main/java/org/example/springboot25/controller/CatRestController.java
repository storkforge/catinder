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

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<CatOutputDTO> getAllCats(Authentication auth) {
        User currentUser = userService.getUserByUserName(auth.getName());
        return catService.getAllCatsByOwner(currentUser);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{catId}")
    public CatOutputDTO getCatById(@PathVariable Long catId, Authentication auth) {
        User currentUser = userService.getUserByUserName(auth.getName());
        return catService.getCatDtoById(catId, currentUser);
    }

    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM')")
    @PostMapping
    public ResponseEntity<CatOutputDTO> createCat(@RequestBody CatInputDTO dto, Authentication auth) {
        User currentUser = userService.getUserByUserName(auth.getName());
        CatOutputDTO created = catService.createCat(dto, currentUser);
        return ResponseEntity.created(URI.create("/api/cats/" + created.getCatId())).body(created);
    }

    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM', 'ADMIN')")
    @PutMapping("/{catId}")
    public ResponseEntity<CatOutputDTO> updateCat(@PathVariable Long catId,
                                                  @RequestBody CatInputDTO dto,
                                                  Authentication auth) {
        User currentUser = userService.getUserByUserName(auth.getName());
        CatOutputDTO updated = catService.updateCat(catId, dto, currentUser);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM', 'ADMIN')")
    @PatchMapping("/{catId}")
    public CatOutputDTO partialUpdateCat(@PathVariable Long catId,
                                         @RequestBody Map<String, Object> updates,
                                         Authentication auth) {
        User currentUser = userService.getUserByUserName(auth.getName());
        return catService.partialUpdateCat(catId, updates, currentUser);
    }

    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM', 'ADMIN')")
    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> deleteCat(@PathVariable Long catId, Authentication auth) {
        User currentUser = userService.getUserByUserName(auth.getName());
        catService.deleteCat(catId, currentUser);
        return ResponseEntity.noContent().build();
    }
}
