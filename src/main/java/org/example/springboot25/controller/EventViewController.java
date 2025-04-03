package org.example.springboot25.controller;

import org.example.springboot25.service.EventService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/events")
public class EventViewController {

    private final EventService eventService;

    public EventViewController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public String showAllEvents(Model model) {
        model.addAttribute("events", eventService.getAllEvents());
        return "event"; 
    }
}
