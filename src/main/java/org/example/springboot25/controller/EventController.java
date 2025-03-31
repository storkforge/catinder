package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.entities.Event;
import org.example.springboot25.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events") // Endpoints
public class EventController {

    private final EventService eventService;

    // Konstruktor injicerar service
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // Hämta alla events
    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    // Returnerar ett enskilt event
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Event getEventById(@PathVariable Long id) {
        return eventService.getEventById(id);
    }

    // Skapar ett nytt event
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Event createEvent(@RequestBody @Valid Event event) {
        return eventService.createEvent(event);
    }

    // Tar bort ett event
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable Long id) {
        eventService.getEventById(id); // Kontrollera om det finns
        eventService.deleteEvent(id);
    }

    // Uppdaterar ett event
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Event updateEvent(@PathVariable Long id, @RequestBody @Valid Event event) {
        return eventService.updateEvent(id, event);
    }

    // Deluppdaterar ett event
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Event patchEvent(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return eventService.patchEvent(id, updates);
    }

}
