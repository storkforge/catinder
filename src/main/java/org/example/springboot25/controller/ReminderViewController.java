package org.example.springboot25.controller;

import org.example.springboot25.entities.Cat;
import org.example.springboot25.entities.Reminder;
import org.example.springboot25.entities.User;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.service.CatService;
import org.example.springboot25.service.ReminderService;
import org.example.springboot25.service.UserService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    public ReminderViewController(ReminderService reminderService, UserService userService, CatService catService) {
        this.reminderService = reminderService;
        this.userService = userService;
        this.catService = catService;

    }
    @GetMapping
    public String showAllReminders(Model model) {
        model.addAttribute("reminders", reminderService.getAllReminders());
        return "reminder/list-reminder";
    }
//visa en specifik reminder
    @GetMapping("/{reminderId}")
    public String showASpecificReminder(@PathVariable Long reminderId, Model model) {
        Reminder reminder = reminderService.getReminderById(reminderId);
        if (reminder == null) {
            throw new NotFoundException("Reminder not found with id " + reminderId);
        }
        model.addAttribute("reminder", reminder);
        return "reminder/show-reminder-details";
    }

    @GetMapping("/{reminderId}/edit")
    public String showEditReminder(@PathVariable Long reminderId, Model model, Principal principal) {
        Reminder reminder = reminderService.getReminderById(reminderId);
        if (reminder == null) {
            throw new NotFoundException("Reminder not found with id " + reminderId);
        }
        model.addAttribute("reminder", reminder);
        User current = userService.findUserByEmail(((OAuth2AuthenticationToken) principal)
                .getPrincipal().getAttribute("email"));
        List<Cat> cats = catService.getAllCatsByUser(current);
        model.addAttribute("cats", cats);
        return "reminder/edit-reminder-details-form";
    }


    @PostMapping("/{reminderId}")
    public String updateReminder(@PathVariable Long reminderId, @ModelAttribute("reminder") Reminder reminder) {
        try { reminderService.updateReminder(reminderId, reminder);
        } catch (NotFoundException e) {
            throw e;
        }catch (Exception e) {
            throw new RuntimeException("Update failed for reminder with id " + reminderId, e);
        }
        return "redirect:/reminders/" + reminderId;
    }

    @GetMapping("/new")
    public String showCreateNewReminderForm(Model model, Principal principal) {
        model.addAttribute("reminder", new Reminder());
        User current = userService.findUserByEmail(((OAuth2AuthenticationToken) principal)
                .getPrincipal().getAttribute("email"));
        List<Cat> cats = catService.getAllCatsByUser(current);
        model.addAttribute("cats", cats);
        return "reminder/creating-a-new-reminder-form";
    }

    @PostMapping
    public String processCreateNewReminderForm(@ModelAttribute("reminder") Reminder reminder,
                                               @RequestParam Long catId, Principal principal) {
        if (principal instanceof OAuth2AuthenticationToken oauthToken) {
            OAuth2User oauthUser = oauthToken.getPrincipal();
            String email = oauthUser.getAttribute("email");
            User user = userService.findUserByEmail(email);
            reminder.setUser(user);

            // Hämtar Cat entitet för att sätta det på reminder
            Cat cat = catService.getCatById(catId)
                    .orElseThrow(() -> new NotFoundException("Cat not found with id " + catId));
            reminder.setCatReminderCat(cat);

            reminderService.createReminder(reminder);
        } else {
            throw new IllegalStateException("Unexpected authentication type: " + principal.getClass().getName());
        }
        return "redirect:/reminders";
    }

    @DeleteMapping("/{reminderId}/delete")
    public String deleteReminder(@PathVariable Long reminderId, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            reminderService.deleteReminder(reminderId);
            redirectAttributes.addFlashAttribute("delete_success", "Reminder deleted successfully!");

        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("delete_failed", "Reminder not found with id " + reminderId);
        }
        return "redirect:/reminders";
    }




}