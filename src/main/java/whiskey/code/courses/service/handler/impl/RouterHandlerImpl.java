package whiskey.code.courses.service.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import whiskey.code.courses.bot.CoursesBot;
import whiskey.code.courses.service.handler.MessageHandler;
import whiskey.code.courses.service.handler.RouterHandler;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RouterHandlerImpl implements RouterHandler {

    private final List<MessageHandler> handlers;

    public void route(Update update, CoursesBot bot) {
        for (MessageHandler handler : handlers) {
            if (handler.canHandle(update)) {
                handler.handle(update, bot);
                return;
            }
        }
    }

}
