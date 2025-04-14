package org.example.springboot25.dto;

public class EventParticipantUpdateDTO {
    private String userName;
    private String eventName;

    public EventParticipantUpdateDTO() {
    }

    public EventParticipantUpdateDTO(String userName, String eventName) {
        this.userName = userName;
        this.eventName = eventName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventParticipantUpdateDTO that = (EventParticipantUpdateDTO) o;

        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        return eventName != null ? eventName.equals(that.eventName) : that.eventName == null;
    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + (eventName != null ? eventName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EventParticipantUpdateDTO{" +
                "userName='" + userName + '\'' +
                ", eventName='" + eventName + '\'' +
                '}';
    }

}
