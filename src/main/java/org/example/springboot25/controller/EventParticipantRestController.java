package org.example.springboot25.controller;


import org.example.springboot25.entities.EventParticipant;
import org.example.springboot25.service.EventParticipantService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participants")
public class EventParticipantRestController {

    private final EventParticipantService service;

    public EventParticipantRestController(EventParticipantService service) {
        this.service = service;
    }

    @GetMapping
    public List<EventParticipant> getAll() {
        return service.getAllParticipants();
    }

    @GetMapping("/event/{eventId}")
    public List<EventParticipant> getByEventId(@PathVariable Long eventId) {
        return service.getParticipantsByEventId(eventId);
    }

    @GetMapping("/user/{userId}")
    public List<EventParticipant> getByUserId(@PathVariable Long userId) {
        return service.getParticipantsByUserId(userId);
    }

    @GetMapping("/check")
    public boolean isUserParticipating(@RequestParam Long userId, @RequestParam Long eventId) {
        return service.isUserParticipating(userId, eventId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventParticipant add(@RequestBody EventParticipant participant) {
        return service.addParticipant(participant);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteParticipation(id);
    }

}
