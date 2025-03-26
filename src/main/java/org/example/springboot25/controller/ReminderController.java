package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.entities.Reminder;
import org.example.springboot25.service.ReminderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reminders")
public class ReminderController {

    private final ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Reminder> getAllReminders() {
        return reminderService.getAllReminders();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Reminder getReminderById(@PathVariable Long id) {
        return reminderService.getReminderById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reminder createReminder(@RequestBody @Valid Reminder reminder) {
        return reminderService.createReminder(reminder);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReminder(@PathVariable Long id) {
        reminderService.deleteReminder(id);
    }
}