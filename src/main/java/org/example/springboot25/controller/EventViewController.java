package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.entities.*;
import org.example.springboot25.exceptions.ConflictException;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.service.EventParticipantService;
import org.example.springboot25.service.EventService;
import org.example.springboot25.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/events")
public class EventViewController {
    private static final Logger log = LoggerFactory.getLogger(EventViewController.class);
    private final EventService eventService;
    private final EventParticipantService eventParticipantService;
    private final UserService userService;

    public EventViewController(EventService eventService, EventParticipantService eventParticipantService, UserService userService) {
        this.eventService = eventService;
        this.eventParticipantService = eventParticipantService;
        this.userService = userService;
    }

    @GetMapping
    public String showAllEvents(@RequestParam(value = "q", required = false) String query,Model model) {
        List<Event> events;
        if (query != null && !query.trim().isEmpty()) {
            events = eventService.searchEvents(query);
            model.addAttribute("searchQuery", query);
        } else {
            events = eventService.getAllEvents();
        }
        model.addAttribute("events", events);
        return "event/event-list";
    }

    @GetMapping("/event-details/{eventId}")
    public String showEventDetails(@PathVariable Long eventId, Principal principal, Model model) {
        List<Event> events = eventService.getAllEvents();
        Event selectedEvent = eventService.getEventById(eventId);

        User currentUser = principal instanceof OAuth2AuthenticationToken
                ? userService.findUserByEmail(((OAuth2AuthenticationToken) principal).getPrincipal().getAttribute("email"))
                : userService.findUserByUserName(principal.getName());

        model.addAttribute("events", events);
        model.addAttribute("selectedEvent", selectedEvent);
        model.addAttribute("currentUser", currentUser);
        return "event/event-details";
    }

    @PostMapping("/event-details/{eventId}/cancel")
    public String cancelAttendance(@PathVariable Long eventId, Principal principal, RedirectAttributes redirectAttributes) {
        User currentUser;
        Event event = eventService.getEventById(eventId);
        if (principal instanceof OAuth2AuthenticationToken oauthToken) {
            OAuth2User oauth2User = oauthToken.getPrincipal();
            String email = oauth2User.getAttribute("email");
            currentUser = userService.findUserByEmail(email);
        } else {
            currentUser = userService.findUserByEmail(principal.getName());
        }
        try {
            eventParticipantService.deleteParticipant(currentUser.getUserName(), event.getEventName());
        redirectAttributes.addFlashAttribute("cancelSuccess", "Your attendance has been cancelled!");
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Stop it!");
        }
        return "redirect:/events/event-details/" + eventId;
    }

    @PostMapping("/event-details/{eventId}/attend")
    public String attendEvent(@PathVariable Long eventId, Principal principal, RedirectAttributes redirectAttributes) {
        User currentUser;
        Event event = eventService.getEventById(eventId);
        if (principal instanceof OAuth2AuthenticationToken oauthToken) {
            OAuth2User oauth2User = oauthToken.getPrincipal();
            String email = oauth2User.getAttribute("email");
            currentUser = userService.findUserByEmail(email);
        } else {
            currentUser = userService.findUserByEmail(principal.getName());
        }

        try {
        eventParticipantService.addParticipant(currentUser.getUserName(), event.getEventName());
        redirectAttributes.addFlashAttribute("attendSuccess", "You are now attending the event!");
        } catch (ConflictException e) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/events/event-details/" + eventId;
    }

    @GetMapping("/add")
    String addEventForm(User user,Model model) {
        if (!model.containsAttribute("event")) {
            model.addAttribute("event", new Event());
        }
        return "event/event-add";
    }

    @PostMapping("/add")
    public String processCreateNewEventForm(@ModelAttribute Event event,
                                            Principal principal,
                                            RedirectAttributes redirectAttributes) {
        if (principal instanceof OAuth2AuthenticationToken oauthToken) {
            OAuth2User oauth2User = oauthToken.getPrincipal();
            String email = oauth2User.getAttribute("email");
            User user = userService.findUserByEmail(email);
            event.setUserEventPlanner(user);
        } else if (principal != null) {
            User user = userService.findUserByUserName(principal.getName());
            event.setUserEventPlanner(user);
        } else {
            throw new IllegalStateException("Unexpected authentication type: " + principal.getClass().getName());
        }
        eventService.createEvent(event);
        redirectAttributes.addFlashAttribute("success", "Event created!");
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
            return "error";
        }
    }

    @PutMapping("/{eventId}")
    public String updateEvent(@PathVariable Long eventId,
                              @Valid @ModelAttribute Event event,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "event/event-update";
        }
        Event existingEvent = eventService.getEventById(eventId);
        event.setEventDateTime(existingEvent.getEventDateTime());
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
            return "error";
        }
        return "redirect:/events";
    }
}
