package org.example.springboot25.dto;
import java.time.OffsetDateTime;
import java.util.Objects;

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

    @Override
    public String toString() {
        return "ReminderOutputDTO{" +
                "id=" + id +
                ", reminderType='" + reminderType + '\'' +
                ", reminderInfo='" + reminderInfo + '\'' +
                ", reminderDate=" + reminderDate +
                ", userId=" + userId +
                ", catId=" + catId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReminderOutputDTO that = (ReminderOutputDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(reminderType, that.reminderType) &&
                Objects.equals(reminderInfo, that.reminderInfo) &&
                Objects.equals(reminderDate, that.reminderDate) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(catId, that.catId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reminderType, reminderInfo, reminderDate, userId, catId);
    }


}
