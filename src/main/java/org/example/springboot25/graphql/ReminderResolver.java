package org.example.springboot25.graphql;

import org.example.springboot25.dto.ReminderInputDTO;
import org.example.springboot25.entities.Cat;
import org.example.springboot25.entities.Reminder;
import org.example.springboot25.entities.ReminderType;
import org.example.springboot25.entities.User;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.service.CatService;
import org.example.springboot25.service.ReminderService;
import org.example.springboot25.service.UserService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReminderResolver {

    private final ReminderService reminderService;
    private final UserService userService;
    private final CatService catService; // Inject CatService to fetch Cat by id

    public ReminderResolver(ReminderService reminderService, UserService userService, CatService catService) {
        this.reminderService = reminderService;
        this.userService = userService;
        this.catService = catService;
    }

    @QueryMapping
    public List<Reminder> reminders(Authentication authentication) {
        User current = userService.findUserByUserName(authentication.getName());
        return reminderService.getRemindersByUser(current);
    }

    @MutationMapping
    public Reminder createReminder(@Argument ReminderInputDTO input, Authentication authentication) {
        User current = userService.findUserByUserName(authentication.getName());
        Reminder reminder = new Reminder();

        // Convert the String input to the corresponding ReminderType enum
        try {
            reminder.setReminderType(ReminderType.valueOf(input.getReminderType().trim().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid reminder type provided: " + input.getReminderType());
        }

        reminder.setReminderInfo(input.getReminderInfo());
        reminder.setReminderDate(input.getReminderDate());
        reminder.setUser(current);

        // Retrieve the Cat entity using the catId from the input DTO and set it on the reminder.
        Cat cat = catService.getCatById(input.getCatId())
                .orElseThrow(() -> new NotFoundException("Cat not found with id " + input.getCatId()));
        reminder.setCatReminderCat(cat);

        return reminderService.createReminder(reminder);
    }
}
