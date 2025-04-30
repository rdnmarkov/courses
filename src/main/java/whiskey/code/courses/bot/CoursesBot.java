package whiskey.code.courses.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import whiskey.code.courses.config.properties.BotProperties;
import whiskey.code.courses.entity.Course;
import whiskey.code.courses.service.db.CourseService;
import whiskey.code.courses.util.Utils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoursesBot extends TelegramLongPollingBot {

    private final BotProperties botProperties;
    private final CourseService courseService;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Long chatId = update.getMessage().getChatId();

            List<Course> courses = courseService.allCourses();

            SendMessage sendMessage = new SendMessage(String.valueOf(chatId), "Курсы :" + courses.toString());

            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

        } else if (update.hasChannelPost()) {
            Long chatId = update.getChannelPost().getChatId();
            if (chatId.equals(botProperties.getAdminChannel())) {

                SendMessage message = new SendMessage(String.valueOf(chatId),
                        String.valueOf(update.getChannelPost().getMessageId()));

                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }


    @Override
    public String getBotUsername() {
        return botProperties.getName();
    }

    @Override
    public String getBotToken() {
        return botProperties.getKey();
    }
}
