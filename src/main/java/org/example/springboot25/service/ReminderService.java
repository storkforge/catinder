package org.example.springboot25.service;

import org.example.springboot25.entities.Reminder;
import org.example.springboot25.exceptions.ReminderNotFoundException;
import org.example.springboot25.repository.ReminderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ReminderService {

    private final ReminderRepository reminderRepository;

    public ReminderService(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    @Transactional
    public List<Reminder> getAllReminders() {
        return reminderRepository.findAll();
    }

    @Transactional
    public Reminder getReminderById(Long id) {
        return reminderRepository.findById(id)
                .orElseThrow(() -> new ReminderNotFoundException("Påminnelse med id " + id + " hittades inte"));
    }

    @Transactional
    public Reminder createReminder(Reminder reminder) {
        return reminderRepository.save(reminder);
    }

    @Transactional
    public void deleteReminder(Long id) {
        if (!reminderRepository.existsById(id)) {
            throw new ReminderNotFoundException("Påminnelse med id " + id + " hittades inte");
        }
        reminderRepository.deleteById(id);
    }
}