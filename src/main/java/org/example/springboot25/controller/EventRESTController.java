package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.entities.Event;
import org.example.springboot25.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventRESTController {

    private final EventService eventService;

    public EventRESTController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Event getEventById(@PathVariable Long id) {
        return eventService.getEventById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Event createEvent(@RequestBody @Valid Event event) {
        return eventService.createEvent(event);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Event updateEvent(@PathVariable Long id, @RequestBody @Valid Event event) {
        return eventService.updateEvent(id, event);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Event patchEvent(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return eventService.patchEvent(id, updates);
    }
}
