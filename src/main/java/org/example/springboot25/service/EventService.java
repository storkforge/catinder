package org.example.springboot25.service;

import org.example.springboot25.entities.Event;
import org.example.springboot25.exceptions.EventNotFoundException;
import org.example.springboot25.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
                .orElseThrow(() -> new EventNotFoundException("Event with id " + id + " not found"));
    }

    // Skapa ett nytt event och spara i databasen
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    // Ta bort ett event
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    // Uppdaterar ett event
    public Event updateEvent(Long id, Event updatedEvent) {
        Event existing = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event with id " + id + " not found"));

        existing.setEventName(updatedEvent.getEventName());
        existing.setEventDescription(updatedEvent.getEventDescription());
        existing.setEventLocation(updatedEvent.getEventLocation());
        existing.setEventDateTime(updatedEvent.getEventDateTime());

        return eventRepository.save(existing);
    }

    public Event patchEvent(Long id, Map<String, Object> updates) {
        Event existing = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event with id " + id + " not found"));

        if (updates.containsKey("eventName")) {
            existing.setEventName((String) updates.get("eventName"));
        }
        if (updates.containsKey("eventDescription")) {
            existing.setEventDescription((String) updates.get("eventDescription"));
        }
        if (updates.containsKey("eventLocation")) {
            existing.setEventLocation((String) updates.get("eventLocation"));
        }
        if (updates.containsKey("eventDateTime")) {
            String dateTimeStr = (String) updates.get("eventDateTime");
            existing.setEventDateTime(LocalDateTime.parse(dateTimeStr));
        }

        return eventRepository.save(existing);
    }
}
