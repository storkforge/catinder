package org.example.springboot25.controller;

import org.example.springboot25.entities.Event;
import org.example.springboot25.entities.EventParticipant;
import org.example.springboot25.entities.User;
import org.example.springboot25.service.EventParticipantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/participants")
public class EventParticipantViewController {

    private final EventParticipantService service;

    public EventParticipantViewController(EventParticipantService service) {
        this.service = service;
    }

    @GetMapping
    public String viewAll(Model model) {
        model.addAttribute("participants", service.getAllParticipants());
        return "participants/list";
    }

    @GetMapping("/event")
    public String viewByEvent(@RequestParam Long eventId, Model model) {
        model.addAttribute("participants", service.getParticipantsByEventId(eventId));
        return "participants/event-participant";
    }

    @GetMapping("/user")
    public String viewByUser(@RequestParam Long userId, Model model) {
        model.addAttribute("participants", service.getEventByUserId(userId));
        return "participants/user-event";
    }

    @GetMapping("/add")
    public String showForm(Model model) {
        if (!model.containsAttribute("participant")) {
            EventParticipant participant = new EventParticipant();
            participant.setUser(new User());       // 👈 viktigt!
            participant.setEvent(new Event());     // 👈 viktigt!
            model.addAttribute("participant", participant);
        }
        return "participants/add";
    }


    @PostMapping("/add")
    public String addParticipant(@ModelAttribute EventParticipant participant,
                                 RedirectAttributes redirectAttributes) {
        service.addParticipant(participant);
        redirectAttributes.addFlashAttribute("success", "Deltagaren har lagts till!");
        return "redirect:/participants/add";
    }

}
