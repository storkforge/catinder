package org.example.springboot25.repository;

import org.example.springboot25.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findByEventName(String eventName);
    List<Event> findByEventNameContainingIgnoreCase(String keyword);

}
