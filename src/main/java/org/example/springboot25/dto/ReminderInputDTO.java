package org.example.springboot25.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class ReminderInputDTO {

    @NotNull
    @NotBlank(message = "Reminder type is required")
    @Size(max = 255, message = "Reminder type must be less than 255 characters")
    private String reminderType;

    @NotNull
    @NotBlank(message = "Reminder info is required")
    @Size(max = 1000, message = "Reminder info must be less than 1000 characters")
    private String reminderInfo;

    @FutureOrPresent
    @NotNull(message = "Reminder date is required")
    private LocalDateTime reminderDate;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Cat ID is required")
    private Long catId;


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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCatId() {
        return catId;
    }

    public void setCatId(Long catId) {
        this.catId = catId;
    }
}
