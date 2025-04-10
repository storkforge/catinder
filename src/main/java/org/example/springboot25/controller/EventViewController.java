package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.entities.Event;
import org.example.springboot25.entities.Post;
import org.example.springboot25.entities.User;
import org.example.springboot25.entities.UserRole;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.repository.UserRepository;
import org.example.springboot25.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/events")
public class EventViewController {

    private final EventService eventService;

    //Todo: Remove when security is implemented
    @Autowired
    private UserRepository userRepository;

    public EventViewController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public String showAllEvents(Model model) {
        model.addAttribute("events", eventService.getAllEvents());
        return "event/event-list";
    }

    @GetMapping("/add")
    String addEventForm(Model model) {
        if (!model.containsAttribute("event")) {
            model.addAttribute("event", new Post());
        }
        return "event/event-add";
    }

    @PostMapping("/add")
    String addEventForm(@Valid @ModelAttribute Event event, Model model, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "event/event-add";
        }
        try {
            // Todo: Change/fetch to actual user when implementing security
            User dummyUser = userRepository.findByUserName("dummyUser")
                    .orElseGet(() -> {
                        User newDummy = new User();
                        newDummy.setUserFullName("Dummy User");
                        newDummy.setUserName("dummyUser");
                        newDummy.setUserEmail("dummy@example.com");
                        newDummy.setUserLocation("DummyVille");
                        newDummy.setUserRole(UserRole.BASIC);
                        newDummy.setUserAuthProvider("testProvider");
                        newDummy.setUserPassword("dummyPassword");
                        return userRepository.save(newDummy);
                    });
            //Todo: change to real user when security is implemented
            event.setUserEventPlanner(dummyUser);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            model.addAttribute("formattedEventDateTime", event.getEventDateTime().format(formatter));

            event.setEventDateTime(event.getEventDateTime());
            eventService.createEvent(event);
            redirectAttributes.addFlashAttribute("success", "Event created successfully!");
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "event/event-add";
        }
        return "redirect:/events";
    }

    @GetMapping("/{eventId}/edit")
    public String showUpdateForm(@PathVariable Long eventId, Model model) {
        try {
            Event event = eventService.getEventById(eventId);
            model.addAttribute("event", event);
            return "event/event-update";
        } catch (NotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
            return "error-page";
        }
    }

    @PutMapping("/{eventId}")
    public String updateEvent(@PathVariable Long eventId, @Valid @ModelAttribute Event event, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "event/event-update";
        }
        try {
            eventService.updateEvent(eventId, event);
            redirectAttributes.addFlashAttribute("update_success", "Event Updated!");
        } catch (Error ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/events/" + eventId + "/edit";
        }
        return "redirect:/events/" + eventId + "/edit";
    }

    @DeleteMapping("/{eventId}")
    String deleteEvent(@PathVariable Long eventId, RedirectAttributes redirectAttributes, Model model) {
        try {
            eventService.deleteEvent(eventId);
            redirectAttributes.addFlashAttribute("delete_success", "Event deleted!");
        } catch (NotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
            return "error-page";
        }
        return "redirect:/events";
    }
}
