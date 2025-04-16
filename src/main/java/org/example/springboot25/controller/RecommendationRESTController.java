package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.entities.Recommendation;
import org.example.springboot25.entities.User;
import org.example.springboot25.entities.UserRole;
import org.example.springboot25.service.RecommendationService;
import org.example.springboot25.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationRESTController {

    private final RecommendationService recommendationService;
    private final UserService userService;

    public RecommendationRESTController(RecommendationService recommendationService, UserService userService) {
        this.recommendationService = recommendationService;
        this.userService = userService;
    }

    // Helper method for owner/admin check
    private boolean isNotOwnerOrAdmin(Recommendation rec, User currentUser) {
        boolean isOwner = rec.getUser().getUserId().equals(currentUser.getUserId());
        boolean isAdmin = currentUser.getUserRole() == UserRole.ADMIN;
        return !(isOwner || isAdmin);
    }

    // Authenticated users can fetch their own recommendations
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Recommendation> getAllRecommendations(Authentication auth) {
        User currentUser = (User) auth.getPrincipal();
        return recommendationService.getRecommendationsByUser(currentUser);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Recommendation getById(@PathVariable Long id, Authentication auth) {
        Recommendation rec = recommendationService.getRecommendationById(id);
        User current = userService.findUserByUserName(auth.getName());

        if (isNotOwnerOrAdmin(rec, current)) {
            throw new AccessDeniedException("You can only access your own recommendations.");
        }

        return rec;
    }

    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Recommendation createRecommendation(@RequestBody @Valid Recommendation recommendation, Authentication auth) {
        User currentUser = userService.findUserByUserName(auth.getName());
        recommendation.setUser(currentUser);
        return recommendationService.createRecommendation(recommendation);
    }

    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM', 'ADMIN')")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Recommendation patch(@PathVariable Long id, @RequestBody Map<String, Object> updates, Authentication auth) {
        Recommendation existing = recommendationService.getRecommendationById(id);
        User current = userService.findUserByUserName(auth.getName());

        if (isNotOwnerOrAdmin(existing, current)) {
            throw new AccessDeniedException("You can only update your own recommendations.");
        }

        return recommendationService.patchRecommendation(id, updates);
    }

    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM', 'ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, Authentication auth) {
        Recommendation rec = recommendationService.getRecommendationById(id);
        User current = userService.findUserByUserName(auth.getName());

        if (isNotOwnerOrAdmin(rec, current)) {
            throw new AccessDeniedException("You can only delete your own recommendations.");
        }

        recommendationService.deleteRecommendation(id);
    }
}
