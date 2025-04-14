package org.example.springboot25.mapper;

import org.example.springboot25.dto.EventInputDTO;
import org.example.springboot25.dto.EventOutputDTO;
import org.example.springboot25.dto.EventUpdateDTO;
import org.example.springboot25.entities.Event;
import org.example.springboot25.entities.User;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.ZoneOffset;

@Component
public class EventMapper {


    private final UserMapper userMapper;

    public EventMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Event toEvent(EventInputDTO dto, User user) {
        Event event = new Event();
        event.setEventName(dto.getEventName());
        event.setEventDescription(dto.getEventDescription());
        event.setEventLocation(dto.getEventLocation());
        // Time zone is discarded intentionally. All times assumed to be local.
        // GraphQL uses OffsetDateTime, but we store as LocalDateTime in entity for simplicity.
        // Converting OffsetDateTime to LocalDateTime (timezone information is lost)
        event.setEventDateTime(dto.getEventDateTime().toLocalDateTime());
        event.setUserEventPlanner(user);
        return event;
    }

    public void updateEventFromDTO(EventUpdateDTO dto, Event event) {
        if (dto.getEventName() != null) {
            event.setEventName(dto.getEventName());
        }
        if (dto.getEventDescription() != null) {
            event.setEventDescription(dto.getEventDescription());
        }
        if (dto.getEventLocation() != null) {
            event.setEventLocation(dto.getEventLocation());
        }
        if (dto.getEventDateTime() != null) {
            event.setEventDateTime(dto.getEventDateTime().toLocalDateTime());
        }
    }


    public EventOutputDTO toDTO(Event event) {
        EventOutputDTO dto = new EventOutputDTO();
        dto.setEventId(event.getEventId());
        dto.setEventName(event.getEventName());
        dto.setEventDescription(event.getEventDescription());
        dto.setEventLocation(event.getEventLocation());

        if (event.getEventDateTime() != null) {
            // Get offset based on system timezone rules (handles DST)
            ZoneOffset offset = ZoneOffset.systemDefault().getRules().getOffset(event.getEventDateTime());
            dto.setEventDateTime(event.getEventDateTime().atOffset(offset));
        }

        if (event.getUserEventPlanner() != null) {
            dto.setUserEventPlanner(userMapper.toDto(event.getUserEventPlanner()));
        }

        return dto;
    }

}
