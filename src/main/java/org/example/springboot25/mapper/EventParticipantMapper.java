package org.example.springboot25.mapper;

import org.example.springboot25.dto.EventParticipantInputDTO;
import org.example.springboot25.dto.EventParticipantOutputDTO;
import org.example.springboot25.dto.EventParticipantUpdateDTO;
import org.example.springboot25.entities.EventParticipant;
import org.example.springboot25.entities.User;
import org.example.springboot25.entities.Event;
import org.springframework.stereotype.Component;

@Component
public class EventParticipantMapper {

    public EventParticipant toEntityInput(EventParticipantInputDTO dto, User user, Event event) {
        EventParticipant ep = new EventParticipant();
        ep.setUser(user);
        ep.setEvent(event);
        return ep;
    }

    public EventParticipant toEntityUpdate(EventParticipantUpdateDTO dto, User user, Event event) {
        EventParticipant ep = new EventParticipant();
        ep.setUser(user);
        ep.setEvent(event);
        return ep;
    }

    public EventParticipantOutputDTO toDTO(EventParticipant entity) {
        EventParticipantOutputDTO dto = new EventParticipantOutputDTO();
        dto.setId(entity.getEventParticipantId());
        dto.setUserName(entity.getUser().getUserName());
        dto.setEventName(entity.getEvent().getEventName());
        return dto;
    }
}
