package org.example.springboot25.service;

import org.example.springboot25.entities.Event;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<Event> searchEvents(String query) {
        return eventRepository.findByEventNameContainingIgnoreCase(query);
    }


    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event with id " + id + " not found"));
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event with id " + id + " not found"));
        eventRepository.delete(event);
    }

    public Event updateEvent(Long id, Event updatedEvent) {
        Event existing = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event with id " + id + " not found"));

        existing.setEventName(updatedEvent.getEventName());
        existing.setEventDescription(updatedEvent.getEventDescription());
        existing.setEventLocation(updatedEvent.getEventLocation());
        //existing.setEventDateTime(updatedEvent.getEventDateTime());

        return eventRepository.save(existing);
    }

    public Event patchEvent(Long id, Map<String, Object> updates) {
        Event existing = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event with id " + id + " not found"));

        if (updates.get("eventName") instanceof String name) {
            existing.setEventName(name);
        }

        if (updates.get("eventDescription") instanceof String description) {
            existing.setEventDescription(description);
        }

        if (updates.get("eventLocation") instanceof String location) {
            existing.setEventLocation(location);
        }

//        if (updates.get("eventDateTime") instanceof String dateTimeStr) {
//            try {
//                existing.setEventDateTime(LocalDateTime.parse(dateTimeStr));
//            } catch (DateTimeParseException e) {
//                throw new IllegalArgumentException("Invalid format for 'eventDateTime'. Use ISO 8601, e.g. 2025-04-01T14:00");
//            }
//        }

        return eventRepository.save(existing);
    }
}
