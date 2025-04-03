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
    public String participantsHome() {
        return "participants/participants";
    }

    @GetMapping("/list")
    public String viewAll(Model model) {
        model.addAttribute("participants", service.getAllParticipants());
        return "participants/participants-list";
    }

    @GetMapping("/by-event")
    public String showEventSearchForm() {
        return "participants/participants-by-event";
    }

    @GetMapping("/by-event/search")
    public String viewByEventName(@RequestParam String eventName, Model model) {
        model.addAttribute("participants", service.getParticipantsByEventName(eventName));
        return "participants/participants-by-event";
    }

    @GetMapping("/by-user")
    public String showUserSearchForm() {
        return "participants/participants-by-user";
    }

    @GetMapping("/by-user/search")
    public String viewByUserName(@RequestParam String userName, Model model) {
        model.addAttribute("participants", service.getParticipantsByUserName(userName));
        return "participants/participants-by-user";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        if (!model.containsAttribute("participant")) {
            model.addAttribute("participant", new EventParticipant(new User(), new Event()));
        }
        return "participants/participants-add";
    }

    @PostMapping("/add")
    public String addParticipant(@RequestParam String userName,
                                 @RequestParam String eventName,
                                 RedirectAttributes redirectAttributes) {
        try {
            service.addParticipant(userName, eventName);
            redirectAttributes.addFlashAttribute("success", "Deltagaren har lagts till!");
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
            service.deleteParticipant(userName, eventName);
            redirectAttributes.addFlashAttribute("success", "Deltagaren har tagits bort!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/participants/delete";
    }

    @GetMapping("/update")
    public String showPatchForm() {
        return "participants/participants-update";
    }

    @PostMapping("/update")
    public String patchParticipant(@RequestParam String userName,
                                   @RequestParam String newUserName,
                                   @RequestParam String eventName,
                                   @RequestParam String newEventName,
                                   RedirectAttributes redirectAttributes) {
        try {
            service.patchEventForParticipant(userName, newUserName, eventName, newEventName);
            redirectAttributes.addFlashAttribute("success", "The participant has been successfully updated.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/participants/update";
    }

}