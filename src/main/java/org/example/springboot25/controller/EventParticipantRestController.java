package org.example.springboot25.controller;

import org.example.springboot25.entities.EventParticipant;
import org.example.springboot25.entities.User;
import org.example.springboot25.entities.UserRole;
import org.example.springboot25.service.EventParticipantService;
import org.example.springboot25.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participants")
public class EventParticipantRestController {

    private final EventParticipantService service;
    private final UserService userService;

    public EventParticipantRestController(EventParticipantService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    private boolean isNotOwnerOrAdmin(String requestedUserName, User currentUser) {
        boolean isOwner = requestedUserName.equals(currentUser.getUserName());
        boolean isAdmin = currentUser.getUserRole() == UserRole.ADMIN;
        return !(isOwner || isAdmin);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<EventParticipant> getAllParticipants(Authentication auth) {
        User current = userService.findUserByUserName(auth.getName());
        return service.getParticipantsByUserName(current.getUserName());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user")
    public List<EventParticipant> getParticipantsByUserName(@RequestParam String userName, Authentication auth) {
        User current = userService.findUserByUserName(auth.getName());
        if (isNotOwnerOrAdmin(userName, current)) {
            throw new AccessDeniedException("You can only view your own participation data.");
        }
        return service.getParticipantsByUserName(userName);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/event")
    public List<EventParticipant> getParticipantsByEventName(@RequestParam String eventName) {
        return service.getParticipantsByEventName(eventName);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/specific")
    public EventParticipant getParticipant(@RequestParam String userName, @RequestParam String eventName, Authentication auth) {
        User current = userService.findUserByUserName(auth.getName());

        if (isNotOwnerOrAdmin(userName, current)) {
            throw new AccessDeniedException("You can only view your own participation data.");
        }
        return service.getParticipant(userName, eventName);
    }

    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventParticipant addParticipant(@RequestParam String userName, @RequestParam String eventName, Authentication auth) {
        User current = userService.findUserByUserName(auth.getName());

        if (isNotOwnerOrAdmin(userName, current)) {
            throw new AccessDeniedException("You can only view your own participation data.");
        }
        return service.addParticipant(userName, eventName);
    }

    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM', 'ADMIN')")
    @PatchMapping
    public EventParticipant patchEventForParticipant(@RequestParam String userName, @RequestParam String newUserName, @RequestParam String eventName, @RequestParam String newEventName, Authentication auth) {
        User current = userService.findUserByUserName(auth.getName());

        if (isNotOwnerOrAdmin(userName, current)) {
            throw new AccessDeniedException("You can only view your own participation data.");
        }
        return service.patchEventForParticipant(userName, newUserName, eventName, newEventName);
    }

    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM', 'ADMIN')")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteParticipant(@RequestParam String userName, @RequestParam String eventName, Authentication auth) {
        User current = userService.findUserByUserName(auth.getName());

        if (isNotOwnerOrAdmin(userName, current)) {
            throw new AccessDeniedException("You can only remove your own participation.");
        }
        service.deleteParticipant(userName, eventName);
    }
}