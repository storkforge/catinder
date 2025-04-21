package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.dto.ReminderInputDTO;
import org.example.springboot25.entities.*;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.service.CatService;
import org.example.springboot25.service.ReminderService;
import org.example.springboot25.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/reminders")
public class ReminderViewController {

    private final ReminderService reminderService;
    private final UserService userService;
    private final CatService catService;
    private static final Logger logger = LoggerFactory.getLogger(ReminderViewController.class);


    public ReminderViewController(ReminderService reminderService, UserService userService, CatService catService) {
        this.reminderService = reminderService;
        this.userService = userService;
        this.catService = catService;

    }

    private User getCurrentUser(Principal principal) {
        if (principal instanceof OAuth2AuthenticationToken oauth) {
            OAuth2User oauthUser = oauth.getPrincipal();
            String email = oauthUser.getAttribute("email");
            return userService.findUserByEmail(email);
        }
        throw new IllegalStateException("Unexpected authentication type: " + principal.getClass().getName());
    }

    @GetMapping
    public String showAllReminders(Model model, Principal principal) {
        User current = getCurrentUser(principal);
        model.addAttribute("reminders", reminderService.getRemindersByUser(current));
        return "reminder/list-reminder";
    }

    //visa en specifik reminder
    @GetMapping("/{reminderId}")
    public String showASpecificReminder(@PathVariable Long reminderId, Model model, Principal principal) {
        Reminder reminder = reminderService.getReminderById(reminderId);
        User current = getCurrentUser(principal);
        if (!reminder.getUser().getUserId().equals(current.getUserId())) {
            throw new AccessDeniedException("You can only view your own reminders");
        }
        model.addAttribute("reminder", reminder);
        return "reminder/show-reminder-details";
    }

    @GetMapping("/{reminderId}/edit")
    public String showEditReminder(@PathVariable Long reminderId, Model model, Principal principal) {
        Reminder reminder = reminderService.getReminderById(reminderId);
        User current = getCurrentUser(principal);
        if (!reminder.getUser().getUserId().equals(current.getUserId())) {
            throw new AccessDeniedException("You can only edit your own reminder");
        }
        model.addAttribute("reminder", reminder);
        List<Cat> cats = catService.getAllCatsByUser(current);
        model.addAttribute("cats", cats);
        model.addAttribute("currentUser", current);
        return "reminder/edit-reminder-details-form";
    }


    @PostMapping("/{reminderId}")
    public String updateReminder(@PathVariable Long reminderId, @Valid @ModelAttribute("reminder") ReminderInputDTO reminderInput,
                                 BindingResult bindingResult, Principal principal,Model model) {
        Reminder persistedReminder = reminderService.getReminderById(reminderId);

        User currentUser = getCurrentUser(principal);

        if (!persistedReminder.getUser().getUserId().equals(currentUser.getUserId())
                && currentUser.getUserRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("You can only update your own reminders");
        }

        if (bindingResult.hasErrors()) {
            List<Cat> cats = catService.getAllCatsByUser(currentUser);
            model.addAttribute("cats", cats);
            model.addAttribute("currentUser", currentUser);
            return "reminder/edit-reminder-details-form";
        }

        persistedReminder.setReminderType(ReminderType.valueOf(reminderInput.getReminderType()));
        persistedReminder.setReminderDate(reminderInput.getReminderDate());
        persistedReminder.setReminderInfo(reminderInput.getReminderInfo());

        reminderService.updateReminder(reminderId, persistedReminder);
        return "redirect:/reminders/" + reminderId;
    }

    @GetMapping("/new")
    public String showCreateNewReminderForm(Model model, Principal principal) {
        model.addAttribute("reminder", new Reminder());
        User current = getCurrentUser(principal);
        List<Cat> cats = catService.getAllCatsByUser(current);
        model.addAttribute("cats", cats);
        model.addAttribute("currentUser", current);
        return "reminder/creating-a-new-reminder-form";
    }

    @PostMapping
    public String processCreateNewReminderForm(@Valid @ModelAttribute("reminder") ReminderInputDTO reminderInput,
                                               BindingResult bindingResult,
                                               @RequestParam Long catId, Principal principal, Model model) {
        User user = getCurrentUser(principal);
        if (bindingResult.hasErrors()) {
            User current = getCurrentUser(principal);
            List<Cat> cats = catService.getAllCatsByUser(current);
            model.addAttribute("cats", cats);
            return "reminder/creating-a-new-reminder-form";
        }


        Cat cat = catService.getCatById(catId).orElseThrow(() -> new NotFoundException("Cat not found with id " + catId));

        if (!cat.getUser().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("You do not own this cat");
        }

        Reminder reminder = new Reminder();
        reminder.setReminderType(ReminderType.valueOf(reminderInput.getReminderType()));
        reminder.setReminderInfo(reminderInput.getReminderInfo());
        reminder.setReminderDate(reminderInput.getReminderDate());
        reminder.setUser(user);
        reminder.setCatReminderCat(cat);

        reminderService.createReminder(reminder);
        return "redirect:/reminders";
    }

    @DeleteMapping("/{reminderId}/delete")
    public String deleteReminder(@PathVariable Long reminderId, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            Reminder reminder = reminderService.getReminderById(reminderId);
            User currentUser = getCurrentUser(principal);

            if (!reminder.getUser().getUserId().equals(currentUser.getUserId())
                    && currentUser.getUserRole() != UserRole.ADMIN) {
                logger.warn("Access denied: User {} attempted to update reminder {} owned by {}",
                currentUser.getUserId(), reminderId, reminder.getUser().getUserId());
                throw new AccessDeniedException("You can only delete your own reminders");
            }

            reminderService.deleteReminder(reminderId);
            redirectAttributes.addFlashAttribute("delete_success", "Reminder deleted successfully!");

        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("delete_failed", "Reminder not found with id " + reminderId);
        }
        return "redirect:/reminders";
    }


}