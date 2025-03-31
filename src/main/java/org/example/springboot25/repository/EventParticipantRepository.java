package org.example.springboot25.repository;

import org.example.springboot25.entities.Event;
import org.example.springboot25.entities.EventParticipant;
import org.example.springboot25.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventParticipantRepository extends JpaRepository<EventParticipant, Long> {

    List<EventParticipant> findByEvent(Event event);

    List<EventParticipant> findByEvent_EventId(Long eventId);

    List<EventParticipant> findByUser(User user);

    List<EventParticipant> findByUser_UserId(Long userId);

    boolean existsByUserAndEvent(User user, Event event);
    boolean existsByUser_UserIdAndEvent_EventId(Long userId, Long eventId);

    Optional<EventParticipant> findByUserAndEvent(User user, Event event);
    Optional<EventParticipant> findByUser_UserIdAndEvent_EventId(Long userId, Long eventId);

    long countByEvent(Event event);

}
