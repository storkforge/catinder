package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.dto.EventInputDTO;
import org.example.springboot25.dto.EventOutputDTO;
import org.example.springboot25.dto.EventUpdateDTO;
import org.example.springboot25.entities.Event;
import org.example.springboot25.entities.User;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.mapper.EventMapper;
import org.example.springboot25.service.EventService;
import org.example.springboot25.service.UserService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class EventGraphQLController {

    private final EventService eventService;
    private final EventMapper eventMapper;
    private final UserService userService;

    public EventGraphQLController(EventService eventService, EventMapper eventMapper, UserService userService) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
        this.userService = userService;
    }

    @QueryMapping
    public List<EventOutputDTO> getAllEvents() {
        return eventService.getAllEvents()
                .stream()
                .map(eventMapper::toDTO)
                .toList();
    }

    @QueryMapping
    public EventOutputDTO getEventById(@Argument Long eventId) {
        Event event = eventService.getEventById(eventId);
        if (event == null) {
            throw new NotFoundException("Event not found with ID: " + eventId);
        }
        return eventMapper.toDTO(event);
    }

    // ToDO: Needs getEventByName in Event-service for this to work
//    @QueryMapping
//    public EventOutputDTO getEventByName(@Argument String eventName) {
//        return eventMapper.toDTO(eventService.getEventByName(eventName));
//    }

    @MutationMapping
    public EventOutputDTO createEvent(@Argument("input") @Valid EventInputDTO input) {
        User user = userService.findUserById(input.getUserId());
        if (user == null) {
            throw new NotFoundException("User not found with ID: " + input.getUserId());
        }
        Event event = eventMapper.toEvent(input, user);
        return eventMapper.toDTO(eventService.createEvent(event));
    }

    @MutationMapping
    public EventOutputDTO updateEvent(@Argument Long eventId, @Argument("input") @Valid EventUpdateDTO input) {
        Event event = eventService.getEventById(eventId);
        if (event == null) {
            throw new NotFoundException("Event not found with ID: " + eventId);
        }
        eventMapper.updateEventFromDTO(input, event);
        return eventMapper.toDTO(eventService.updateEvent(eventId, event));
    }

    @MutationMapping
    public boolean deleteEvent(@Argument Long eventId) {
        try {
            eventService.deleteEvent(eventId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}

