package org.example.springboot25.mapper;

import org.example.springboot25.dto.ReminderInputDTO;
import org.example.springboot25.dto.ReminderOutputDTO;
import org.example.springboot25.dto.ReminderUpdateDTO;
import org.example.springboot25.entities.Cat;
import org.example.springboot25.entities.Reminder;
import org.example.springboot25.entities.User;
import org.springframework.stereotype.Component;

@Component
public class ReminderMapper {

    public Reminder toEntityInput(ReminderInputDTO input, User user, Cat cat) {
        Reminder reminder = new Reminder();
        reminder.setReminderType(input.getReminderType());
        reminder.setReminderInfo(input.getReminderInfo());
        reminder.setReminderDate(input.getReminderDate());
        reminder.setUser(user);
        reminder.setCatReminderCat(cat);
        return reminder;
    }

    public ReminderOutputDTO toDTO(Reminder reminder) {
        ReminderOutputDTO dto = new ReminderOutputDTO();
        dto.setId(reminder.getReminderId());
        dto.setReminderType(reminder.getReminderType());
        dto.setReminderInfo(reminder.getReminderInfo());
        dto.setReminderDate(reminder.getReminderDate());
        dto.setUserId(reminder.getUser().getUserId());
        dto.setCatId(reminder.getCatReminderCat().getCatId());
        return dto;
    }

    public Reminder toEntityUpdate(ReminderUpdateDTO input, Reminder existingReminder, User user, Cat cat) {
        if (input.getReminderType() != null) existingReminder.setReminderType(input.getReminderType());
        if (input.getReminderInfo() != null) existingReminder.setReminderInfo(input.getReminderInfo());
        if (input.getReminderDate() != null) existingReminder.setReminderDate(input.getReminderDate());
        existingReminder.setUser(user);
        existingReminder.setCatReminderCat(cat);
        return existingReminder;
    }
}

