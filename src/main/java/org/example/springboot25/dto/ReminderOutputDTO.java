package org.example.springboot25.dto;
import java.time.OffsetDateTime;

public class ReminderOutputDTO {

    private Long id;
    private String reminderType;
    private String reminderInfo;
    private OffsetDateTime reminderDate;
    private Long userId;
    private Long catId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public OffsetDateTime getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(OffsetDateTime reminderDate) {
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
