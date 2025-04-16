package org.example.springboot25.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.example.springboot25.dto.ReminderInputDTO;
import org.example.springboot25.dto.ReminderOutputDTO;
import org.example.springboot25.dto.ReminderUpdateDTO;
import org.example.springboot25.entities.Cat;
import org.example.springboot25.entities.Reminder;
import org.example.springboot25.entities.User;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.mapper.ReminderMapper;
import org.example.springboot25.service.CatService;
import org.example.springboot25.service.ReminderService;
import org.example.springboot25.service.UserService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ReminderGraphQLController {

    private final ReminderService reminderService;
    private final ReminderMapper reminderMapper;
    private final UserService userService;
    private final CatService catService;

    public ReminderGraphQLController(ReminderService reminderService, ReminderMapper reminderMapper, UserService userService, CatService catService) {
        this.reminderService = reminderService;
        this.reminderMapper = reminderMapper;
        this.userService = userService;
        this.catService = catService;
    }

    @QueryMapping
    public List<ReminderOutputDTO> getAllReminders() {
        return reminderService.getAllReminders().stream()
                .map(reminderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @QueryMapping
    public ReminderOutputDTO getReminderById(@Argument Long id) {
        Reminder reminder = reminderService.getReminderById(id);
        return reminderMapper.toDTO(reminder);
    }

    @MutationMapping
    public ReminderOutputDTO createReminder(@Argument("input") @Valid ReminderInputDTO input) {

        if (input.getReminderDate() == null) {
            throw new ValidationException("Reminder date is required");
        }
        if (input.getUserId() == null) {
            throw new ValidationException("User ID is required");
        }

        User user = userService.findUserById(input.getUserId());
        Cat cat = catService.getCatById(input.getCatId())
                .orElseThrow(() -> new NotFoundException("Cat with ID: " + input.getCatId() + " not found"));
        Reminder reminder = reminderService.createReminder(reminderMapper.toEntityInput(input, user, cat));
        return reminderMapper.toDTO(reminder);
    }

    @MutationMapping
    public ReminderOutputDTO updateReminder(@Argument Long id, @Argument("input") ReminderUpdateDTO input) {
        Reminder existing = reminderService.getReminderById(id);

        User user;
        if (input.getUserId() != null) {
            user = userService.findUserById(input.getUserId());
        } else {
            user = existing.getUser();
        }

        Cat cat;
        if (input.getCatId() != null) {
            cat = catService.getCatById(input.getCatId())
                    .orElseThrow(() -> new NotFoundException("Cat with ID: " + input.getCatId() + " not found") );
        } else {
            cat = existing.getCatReminderCat();
        }

        Reminder updated = reminderService.updateReminder(
                id,
                reminderMapper.toEntityUpdate(input, existing, user, cat)
        );
        return reminderMapper.toDTO(updated);
    }

    @MutationMapping
    public boolean deleteReminder(@Argument Long id) {
        reminderService.deleteReminder(id);
        return true;
    }
}

