package org.example.springboot25.mapper;

import org.example.springboot25.dto.ReminderInputDTO;
import org.example.springboot25.dto.ReminderOutputDTO;
import org.example.springboot25.dto.ReminderUpdateDTO;
import org.example.springboot25.entities.Cat;
import org.example.springboot25.entities.Reminder;
import org.example.springboot25.entities.ReminderType;
import org.example.springboot25.entities.User;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

@Component
public class ReminderMapper {

    public Reminder toEntityInput(ReminderInputDTO input, User user, Cat cat) {
        Reminder reminder = new Reminder();
        reminder.setReminderType(ReminderType.valueOf(input.getReminderType()));
        reminder.setReminderDate(input.getReminderDate());
        reminder.setUser(user);
        reminder.setCatReminderCat(cat);
        return reminder;
    }

    public ReminderOutputDTO toDTO(Reminder reminder) {
        if (reminder == null) {
            throw new IllegalArgumentException("Reminder must not be null");
        }
        ReminderOutputDTO dto = new ReminderOutputDTO();
        dto.setId(reminder.getReminderId());
        dto.setReminderType(reminder.getReminderType().name());
        dto.setReminderInfo(reminder.getReminderInfo());
        if (reminder.getReminderDate() != null) {
            dto.setReminderDate(reminder.getReminderDate().atOffset(ZoneOffset.UTC));
        }
        if (reminder.getUser() != null) {
            dto.setUserId(reminder.getUser().getUserId());
        }
        if (reminder.getCatReminderCat() != null) {
            dto.setCatId(reminder.getCatReminderCat().getCatId());
        }
        return dto;
    }

    public Reminder toEntityUpdate(ReminderUpdateDTO input, Reminder existingReminder, User user, Cat cat) {
        if (input.getReminderType() != null) {
            existingReminder.setReminderType(ReminderType.valueOf(input.getReminderType()));
        }
        if (input.getReminderDate() != null) existingReminder.setReminderDate(input.getReminderDate());
        if (user != null) existingReminder.setUser(user);
        if (cat != null) existingReminder.setCatReminderCat(cat);

        return existingReminder;
    }
}

