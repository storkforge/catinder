package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.entities.Reminder;
import org.example.springboot25.entities.ReminderType;
import org.example.springboot25.entities.User;
import org.example.springboot25.entities.UserRole;
import org.example.springboot25.repository.ReminderRepository;
import org.example.springboot25.service.ReminderService;
import org.example.springboot25.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/reminders")
public class ReminderRESTController {

    private final ReminderService reminderService;
    private final UserService userService;
    private final ReminderRepository reminderRepository;

    public ReminderRESTController(ReminderService reminderService, UserService userService, ReminderRepository reminderRepository) {
        this.reminderService = reminderService;
        this.userService = userService;
        this.reminderRepository = reminderRepository;
    }

    // kolla vem som äger the reminder
    private boolean isNotOwnerOrAdmin(Reminder reminder, User currentUser) {
        boolean isOwner = reminder.getUser().getUserId().equals(currentUser.getUserId());
        boolean isAdmin = currentUser.getUserRole() == UserRole.ADMIN;
        return !(isOwner || isAdmin);
    }

    // bara auth can se egna reminders
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Reminder> getAllReminders(Authentication auth) {
        User current = userService.findUserByUserName(auth.getName());
        return reminderService.getRemindersByUser(current);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Reminder getReminderById(@PathVariable Long id, Authentication auth) {
        Reminder reminder = reminderService.getReminderById(id);
        User current = userService.findUserByUserName(auth.getName());

        if (isNotOwnerOrAdmin(reminder, current)) {
            throw new AccessDeniedException("You can only view your own reminders.");
        }

        return reminder;
    }

    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reminder createReminder(@RequestBody @Valid Reminder reminder, Authentication auth) {
        User current = userService.findUserByUserName(auth.getName());
        reminder.setUser(current);
        return reminderService.createReminder(reminder);
    }
    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM', 'ADMIN')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Reminder updateReminder(@PathVariable Long id,
                                   @RequestBody @Valid Reminder updatedReminder,
                                   Authentication auth) {
        Reminder existing = reminderService.getReminderById(id);
        User current = userService.findUserByUserName(auth.getName());

        if (isNotOwnerOrAdmin(existing, current)) {
            throw new AccessDeniedException("You can only update your own reminders.");
        }

        return reminderService.updateReminder(id, updatedReminder);
    }

    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM', 'ADMIN')")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Reminder patchReminder(@PathVariable Long id,
                                  @RequestBody Map<String, Object> updates,
                                  Authentication auth) {
        Reminder existing = reminderService.getReminderById(id);
        User current = userService.findUserByUserName(auth.getName());

        if (isNotOwnerOrAdmin(existing, current)) {
            throw new AccessDeniedException("You can only update your own reminders.");
        }

        return reminderService.patchReminder(id, updates);
    }

    @PreAuthorize("hasAnyRole('BASIC', 'PREMIUM', 'ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReminder(@PathVariable Long id, Authentication auth) {
        Reminder reminder = reminderService.getReminderById(id);
        User current = userService.findUserByUserName(auth.getName());

        if (isNotOwnerOrAdmin(reminder, current)) {
            throw new AccessDeniedException("You can only delete your own reminders.");
        }

        reminderService.deleteReminder(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/type/{type}")
    @ResponseStatus(HttpStatus.OK)
    public List<Reminder> getRemindersByType(@PathVariable ReminderType type, Authentication auth) {
        User current = userService.findUserByUserName(auth.getName());
        return reminderRepository.findAllByUserAndReminderType(current, type);
    }
}