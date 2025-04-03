package org.example.springboot25.controller;

import org.example.springboot25.service.ReminderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

    @Controller
    @RequestMapping("/reminders-view")
    public class ReminderViewController {

        private final ReminderService reminderService;

        public ReminderViewController(ReminderService reminderService) {
            this.reminderService = reminderService;
        }

        @GetMapping
        public String getRemindersView(Model model) {
            model.addAttribute("reminder", reminderService.getAllReminders());
            return "reminder";
        }
    }