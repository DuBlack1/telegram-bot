package pro.sky.telegrambot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class NotificationTask {
    @Id
    @GeneratedValue
    Long Id;
    Long chatId;
    String message;
    LocalDateTime LocalDateTime;

    public NotificationTask(Long id, Long chatId, String message, java.time.LocalDateTime localDateTime) {
        Id = id;
        this.chatId = chatId;
        this.message = message;
        LocalDateTime = localDateTime;
    }

    public NotificationTask() {
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public java.time.LocalDateTime getLocalDateTime() {
        return LocalDateTime;
    }

    public void setLocalDateTime(java.time.LocalDateTime localDateTime) {
        LocalDateTime = localDateTime;
    }
}
