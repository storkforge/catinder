package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.entities.Event;
import org.example.springboot25.entities.User;
import org.example.springboot25.entities.UserRole;
import org.example.springboot25.service.EventService;
import org.example.springboot25.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventRESTController {

    private final EventService eventService;
    private final UserService userService;

    public EventRESTController(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }

    /**
     * Helper method to check if current user is not the owner and not an admin
     */

    private boolean isNotOwnerOrAdmin(Event event, User currentUser) {
        boolean isOwner = event.getUserEventPlanner().getUserName().equals(currentUser.getUserName());
        boolean isAdmin = currentUser.getUserRole() == UserRole.ADMIN;
        return !(isOwner || isAdmin);
    }

    // Authenticated users can view all events
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    // Authenticated users can view a specific event by ID
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Event getEventById(@PathVariable Long id) {
        return eventService.getEventById(id);
    }

    // BASIC and PREMIUM users can create new events
    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Event createEvent(@RequestBody @Valid Event event, Authentication auth) {
        User currentUser = userService.getUserByUserName(auth.getName());
        event.setUserEventPlanner(currentUser);
        // event.setEventDateTime(LocalDateTime.now());
        return eventService.createEvent(event);
    }

    // Only the event owner or an admin can delete the event
    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM', 'ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable Long id, Authentication auth) {
        Event event = eventService.getEventById(id);
        User currentUser = userService.getUserByUserName(auth.getName());

        if (isNotOwnerOrAdmin(event, currentUser)) {
            throw new AccessDeniedException("You can only delete your own events");
        }
        eventService.deleteEvent(id);
    }

    // Only the event owner or an admin can partially update the event (PATCH)
    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM', 'ADMIN')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Event updateEvent(@PathVariable Long id, @RequestBody @Valid Event event, Authentication auth) {
        Event existing = eventService.getEventById(id);
        User currentUser = userService.getUserByUserName(auth.getName());

        if (isNotOwnerOrAdmin(existing, currentUser)) {
            throw new AccessDeniedException("You can only update your own events");
        }
        return eventService.updateEvent(id, event);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Event patchEvent(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return eventService.patchEvent(id, updates);
    }
}
