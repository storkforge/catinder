package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.entities.Event;
import org.example.springboot25.service.EventService;
import org.springframework.http.HttpStatus;
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

    // GET /api/events/{id} – Returnerar ett enskilt event, status 200 OK
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Event getEventById(@PathVariable Long id) {
        return eventService.getEventById(id);
    }

    // POST /api/events – Skapar ett nytt event, status 201 Created
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Event createEvent(@RequestBody @Valid Event event) {
        return eventService.createEvent(event);
    }

    // DELETE /api/events/{id} – Tar bort ett event, status 204 No Content
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable Long id) {
        eventService.getEventById(id); // Kontrollera om det finns
        eventService.deleteEvent(id);
    }
}
