package org.example.springboot25.controller;

import org.example.springboot25.dto.EventParticipantInputDTO;
import org.example.springboot25.dto.EventParticipantOutputDTO;
import org.example.springboot25.dto.EventParticipantUpdateDTO;
import org.example.springboot25.entities.EventParticipant;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.mapper.EventParticipantMapper;
import org.example.springboot25.service.EventParticipantService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class EventParticipantGraphQLController {

    private final EventParticipantService eventParticipantService;
    private final EventParticipantMapper eventParticipantMapper;


    public EventParticipantGraphQLController(EventParticipantService eventPartService, EventParticipantMapper eventPartMapper) {
        this.eventParticipantService = eventPartService;
        this.eventParticipantMapper = eventPartMapper;
    }

    @QueryMapping
    public List<EventParticipantOutputDTO> getAllEventParticipants() {
        return eventParticipantService.getAllParticipants().stream()
                .map(eventParticipantMapper::toDTO)
                .toList();
    }

    @QueryMapping
    public EventParticipantOutputDTO getEventParticipantById(@Argument Long id) {
        EventParticipant eventParticipant = eventParticipantService.getParticipantById(id);
        return eventParticipantMapper.toDTO(eventParticipant);
    }

    @QueryMapping
    public EventParticipantOutputDTO getEventParticipantByUserName(@Argument String userName) {
        EventParticipant eventParticipant = eventParticipantService.getParticipant(userName, null);
        return eventParticipantMapper.toDTO(eventParticipant);
    }

    @MutationMapping
    public EventParticipantOutputDTO createEventParticipant(@Argument("input") EventParticipantInputDTO input) {
        EventParticipant eventParticipant = eventParticipantService.addParticipant(input.getUserName(), input.getEventName());
        return eventParticipantMapper.toDTO(eventParticipant);
    }

    @MutationMapping
    public EventParticipantOutputDTO updateEventParticipant(
            @Argument("id") Long eventParticipantId,
            @Argument("update") EventParticipantUpdateDTO input) {

        EventParticipant eventParticipant = eventParticipantService.updateParticipant(
                eventParticipantId,
                input.getUserName(),
                input.getEventName()
        );

        return eventParticipantMapper.toDTO(eventParticipant);
    }

    @MutationMapping
    public boolean deleteEventParticipant(@Argument Long id) {
        try {
            eventParticipantService.deleteParticipantById(String.valueOf(id));
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }

}




