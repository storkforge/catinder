package org.example.springboot25.service;

import org.example.springboot25.entities.Event;
import org.example.springboot25.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    // Konstruktor injectar repository
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // Hämta alla events från databasen
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // Hämta ett specifikt event
    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }
    // Skapa ett nytt event och spara i databasen
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    // Ta bort ett event
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}
