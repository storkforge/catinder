package org.example.springboot25.controller;

import org.example.springboot25.entities.Event;
import org.example.springboot25.entities.EventParticipant;
import org.example.springboot25.entities.User;
import org.example.springboot25.service.EventParticipantService;
import org.example.springboot25.service.EventService;
import org.example.springboot25.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/participants")
public class EventParticipantViewController {

    private final EventParticipantService eventParticipantService;
    private final EventService eventService;
    private final UserService userService;

    public EventParticipantViewController(EventParticipantService eventParticipantService, EventService eventService, UserService userService) {
        this.eventParticipantService = eventParticipantService;
        this.userService = userService;
        this.eventService = eventService;
    }

    @GetMapping
    public String participantsHome() {
        return "participants/participants";
    }

    @GetMapping("/list")
    public String viewAll(Model model) {
        model.addAttribute("participants", eventParticipantService.getAllParticipants());
        return "participants/participants-list";
    }

    @GetMapping("/by-event")
    public String showEventSearchForm() {
        return "participants/participants-by-event";
    }

    @GetMapping("/by-event/search")
    public String viewByEventName(@RequestParam String eventName, Model model) {
        model.addAttribute("participants", eventParticipantService.getParticipantsByEventName(eventName));
        return "participants/participants-by-event";
    }

    @GetMapping("/by-user")
    public String showUserSearchForm() {
        return "participants/participants-by-user";
    }

    @GetMapping("/by-user/search")
    public String viewByUserName(@RequestParam String userName, Model model) {
        model.addAttribute("participants", eventParticipantService.getParticipantsByUserName(userName));
        return "participants/participants-by-user";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        if (!model.containsAttribute("participant")) {
            model.addAttribute("participant", new EventParticipant(new User(), new Event()));
        }
        return "participants/participants-add";
    }

//    @PostMapping("/add")
//    public String addParticipant(@RequestParam Long eventId,
//                                 Principal principal,
//                                 RedirectAttributes redirectAttributes) {
//        try {
//            String userName = principal.getName();
//            User user = userService.findUserByUserName(userName);
//            Event event = eventService.getEventById(eventId);
//            eventParticipantService.addParticipant(user.getUserName(), event.getEventName());
//            redirectAttributes.addFlashAttribute("success", "You are now attending the event!");
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("error", e.getMessage());
//        }
//        return "redirect:/events";
//    }

    @PostMapping("/add")
    public String addParticipant(@RequestParam String userName,
                                 @RequestParam String eventName,
                                 RedirectAttributes redirectAttributes) {
        try {
            eventParticipantService.addParticipant(userName, eventName);
            redirectAttributes.addFlashAttribute("success", "You are now attending the event!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/participants/add";
    }

    @GetMapping("/delete")
    public String showDeleteForm() {
        return "participants/participants-delete";
    }

    @PostMapping("/delete")
    public String deleteParticipant(@RequestParam String userName,
                                    @RequestParam String eventName,
                                    RedirectAttributes redirectAttributes) {
        try {
            if (userName == null || userName.isEmpty() || eventName == null || eventName.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Username and event name are required");
                return "redirect:/participants/delete";
            }
            eventParticipantService.deleteParticipant(userName, eventName);
            redirectAttributes.addFlashAttribute("success", "Participant deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/participants/delete";
    }

}
