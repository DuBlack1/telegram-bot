package pro.sky.telegrambot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class NotificationTask {
    @Id
    @GeneratedValue
    Long id;
    Long chatId;
    String message;
    LocalDateTime localDateTime;

    public NotificationTask(Long id, Long chatId, String message, java.time.LocalDateTime localDateTime) {
        this.id = id;
        this.chatId = chatId;
        this.message = message;
        this.localDateTime = localDateTime;
    }

    public NotificationTask() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return localDateTime;
    }

    public void setLocalDateTime(java.time.LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
}
