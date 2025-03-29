package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.entities.Reminder;
import org.example.springboot25.service.ReminderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/reminders")
public class ReminderRESTController {

    private final ReminderService reminderService;

    public ReminderRESTController(ReminderService reminderService) {
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

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Reminder updateReminder(@PathVariable Long id, @RequestBody @Valid Reminder reminder) {
        reminder.setReminderId(id); // 💡 viktigt!
        return reminderService.updateReminder(id, reminder);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Reminder patchReminder(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Set<String> allowedFields = Set.of("reminderType", "reminderInfo", "reminderDate");
        for (String key : updates.keySet()) {
            if (!allowedFields.contains(key)) {
                throw new IllegalArgumentException("Otillåtet fältnamn: " + key);
            }
        }
        return reminderService.patchReminder(id, updates);
    }
}