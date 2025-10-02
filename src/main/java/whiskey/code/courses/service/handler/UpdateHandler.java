package whiskey.code.courses.service.handler;

import org.telegram.telegrambots.meta.api.objects.Update;
import whiskey.code.courses.bot.CoursesBot;

public interface UpdateHandler {
    boolean canHandle(Update update);

    void handle(Update update, CoursesBot bot);
}