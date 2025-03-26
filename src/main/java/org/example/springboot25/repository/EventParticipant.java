package org.example.springboot25.repository;

import org.example.springboot25.entities.Event;
import org.example.springboot25.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventParticipant extends JpaRepository<EventParticipant, Long> {

    List<EventParticipant> findByEventParticipantEvent(Event event);
    List<EventParticipant> findByEventParticipantEvent_EventId(Long eventId);

    List<EventParticipant> findByEventParticipantUser(User user);
    List<EventParticipant> findByEventParticipantUser_UserId(Long userId);

    boolean existsByEventParticipantUserAndEventParticipantEvent(User user, Event event);

    Optional<EventParticipant> findByEventParticipantUserAndEventParticipantEvent(User user, Event event);
}
