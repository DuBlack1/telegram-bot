package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * класс отвечает за ответы пользователю
 */
@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;
    @Autowired
    private NotificationTaskRepository notificationTaskRepository;

    String textStart = "Добро пожаловать!" +
            "Я помогу вам в планировнии вашей жизни. " +
            "Вы отправляете мне собщения  в формате 01.01.2022 20:00 Необходимая задача, а я Вам напоминаю в нужно время";
    String textPlan = "Задача запланированна";
    String textError = "Ошибка в формате сообщения";

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    /**
     * Метод проверяет входящие сообщения и отправляет ответ. Обрабатывает /start и сообщения на запоминание
     * @param updates
     * @return
     */
    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            Long chatId = update.message().chat().id();
            logger.info("Processing update: {}", update);
            if (update.message().text().equals("/start")){
                SendMessage message = new SendMessage(chatId, textStart);
                SendResponse response = telegramBot.execute(message);
            }else{
                String text = update.message().text();
                Pattern PATTERN = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
                Matcher matcher = PATTERN.matcher(text);
                if (matcher.matches()){
                    LocalDateTime localDateTime = LocalDateTime.parse(matcher.group(1), DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                    String message = matcher.group(3);
                    NotificationTask notificationTask = new NotificationTask();
                    notificationTask.setChatId(chatId);
                    notificationTask.setMessage(message);
                    notificationTask.setLocalDateTime(localDateTime);
                    notificationTaskRepository.save(notificationTask);
                    SendMessage message1 = new SendMessage(chatId, textPlan);
                    SendResponse response = telegramBot.execute(message1);
                }else {
                    SendMessage message1 = new SendMessage(chatId, textError);
                    SendResponse response = telegramBot.execute(message1);
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /**
     * Метод отправяющий напоминание о задачах раз в минуту
     */
    @Scheduled(cron = "0 0/1 * * * *")
    public void checkTask1(){
        notificationTaskRepository.findAll().stream()
                .filter(f-> f.getLocalDateTime().equals(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)))
                .collect(Collectors.toList())
                .forEach(f-> {
                    SendMessage message = new SendMessage(f.getChatId(), f.getMessage());
                    SendResponse response = telegramBot.execute(message);
                });
    }
}
