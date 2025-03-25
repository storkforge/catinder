package org.example.springboot25.controller;

import org.example.springboot25.entities.Event;
import org.example.springboot25.service.EventService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // Hämta ett event
    @GetMapping("/{id}")
    public Event getEventById(@PathVariable Long id) {
        return eventService.getEventById(id);
    }

    // Skapa nytt event
    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        return eventService.createEvent(event);
    }

    // Ta bort ett event
    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }
}
