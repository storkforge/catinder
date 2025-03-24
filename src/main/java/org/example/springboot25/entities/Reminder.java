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

    public @NotBlank String getReminderType() {
        return reminderType;
    }

    public void setReminderType(@NotBlank String reminderType) {
        this.reminderType = reminderType;
    }

    public @NotBlank String getReminderInfo() {
        return reminderInfo;
    }

    public void setReminderInfo(@NotBlank String reminderInfo) {
        this.reminderInfo = reminderInfo;
    }

    public @NotNull @FutureOrPresent LocalDateTime getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(@NotNull @FutureOrPresent LocalDateTime reminderDate) {
        this.reminderDate = reminderDate;
    }

    public @NotNull Cat getCatReminderCat() {
        return catReminderCat;
    }

    public void setCatReminderCat(@NotNull Cat catReminderCat) {
        this.catReminderCat = catReminderCat;
    }

}
