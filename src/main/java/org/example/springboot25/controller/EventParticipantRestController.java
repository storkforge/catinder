package org.example.springboot25.controller;


import org.example.springboot25.entities.EventParticipant;
import org.example.springboot25.service.EventParticipantService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participants")
public class EventParticipantRestController {

    private final EventParticipantService service;

    public EventParticipantRestController(EventParticipantService service) {
        this.service = service;
    }

    @GetMapping
    public List<EventParticipant> getAllParticipants() {
        return service.getAllParticipants();
    }

    @GetMapping("/user")
    public List<EventParticipant> getParticipantsByUserName(@RequestParam String userName) {
        return service.getParticipantsByUserName(userName);
    }

    @GetMapping("/event")
    public List<EventParticipant> getParticipantsByEventName(@RequestParam String eventName) {
        return service.getParticipantsByEventName(eventName);
    }

    @GetMapping("/specific")
    public EventParticipant getParticipant(@RequestParam String userName,
                                           @RequestParam String eventName) {
        return service.getParticipant(userName, eventName);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventParticipant addParticipant(@RequestParam String userName, @RequestParam String eventName) {
        return service.addParticipant(userName, eventName);
    }

    @PatchMapping
    public EventParticipant patchEventForParticipant(@RequestParam String userName, @RequestParam String newUserName, @RequestParam String eventName, @RequestParam String newEventName) {
        return service.patchEventForParticipant(userName, newUserName, eventName, newEventName);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteParticipant(@RequestParam String userName, @RequestParam String eventName) {
        service.deleteParticipant(userName, eventName);
    }
}



