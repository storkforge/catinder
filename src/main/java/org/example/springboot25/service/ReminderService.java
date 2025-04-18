package org.example.springboot25.service;

import org.example.springboot25.entities.Reminder;
import org.example.springboot25.entities.ReminderType;
import org.example.springboot25.entities.User;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.repository.ReminderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ReminderService {

    private final ReminderRepository reminderRepository;

    public ReminderService(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    public List<Reminder> getAllReminders() {
        return reminderRepository.findAll();
    }
    public List<Reminder> getRemindersByUser(User user) {
        return reminderRepository.findAllByUser(user);
    }

    public Reminder getReminderById(Long id) {
        return reminderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reminder with id " + id + " not found"));
    }

    public Reminder createReminder(Reminder reminder) {
        return reminderRepository.save(reminder);
    }

    public void deleteReminder(Long id) {
        if (!reminderRepository.existsById(id)) {
            throw new NotFoundException("Reminder with id " + id + " not found");
        }
        reminderRepository.deleteById(id);
    }

    public Reminder updateReminder(Long id, Reminder updated) {
        Reminder existing = reminderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reminder with id " + id + " not found"));

        if (updated.getReminderType() != null) {
            existing.setReminderType(updated.getReminderType());
        }
        if (updated.getReminderInfo() != null) {
            existing.setReminderInfo(updated.getReminderInfo());
        }
        if (updated.getReminderDate() != null) {
            existing.setReminderDate(updated.getReminderDate());
        }
        if (updated.getCatReminderCat() != null) {
            existing.setCatReminderCat(updated.getCatReminderCat());
        }

        return reminderRepository.save(existing);
    }

    public Reminder patchReminder(Long id, Map<String, Object> updates) {
        Reminder existing = reminderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reminder with id " + id + " not found"));

        if (updates.get("reminderType") instanceof String typeStr) {
            try {
                ReminderType reminderType = ReminderType.valueOf(typeStr);
                existing.setReminderType(reminderType);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Reminder type " + typeStr + " not found");
            }
        }
        if (updates.get("reminderInfo") instanceof String info) {
            existing.setReminderInfo(info);
        }
        if (updates.get("reminderDate") instanceof String dateStr) {
            existing.setReminderDate(LocalDateTime.parse(dateStr));
        }

        boolean changesMade = updates.containsKey("reminderType") ||
                updates.containsKey("reminderInfo") ||
                updates.containsKey("reminderDate");

        return changesMade ? reminderRepository.save(existing) : existing;
    }



}
