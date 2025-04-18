package org.example.springboot25.repository;

import org.example.springboot25.entities.Reminder;
import org.example.springboot25.entities.ReminderType;
import org.example.springboot25.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findAllByUser(User user);
    List<Reminder> findAllByUserAndReminderType(User user, ReminderType reminderType);

}

