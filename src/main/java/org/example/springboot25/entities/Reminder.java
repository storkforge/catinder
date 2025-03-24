package org.example.springboot25.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reminderId;

    @NotBlank
    private String reminderType;

    @NotBlank
    private String reminderInfo;

    @NotNull
    @FutureOrPresent
    private LocalDateTime reminderDate;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "reminder_cat_id")
    private Cat catReminderCat;


    public Long getReminderId() {
        return reminderId;
    }

    public String getReminderType() {
        return reminderType;
    }

    public void setReminderType(String reminderType) {
        this.reminderType = reminderType;
    }

    public String getReminderInfo() {
        return reminderInfo;
    }

    public void setReminderInfo(String reminderInfo) {
        this.reminderInfo = reminderInfo;
    }

    public LocalDateTime getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(LocalDateTime reminderDate) {
        this.reminderDate = reminderDate;
    }

    public Cat getCatReminderCat() {
        return catReminderCat;
    }

    public void setCatReminderCat(Cat catReminderCat) {
        this.catReminderCat = catReminderCat;
    }

}
