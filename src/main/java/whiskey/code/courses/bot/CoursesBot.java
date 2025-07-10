package whiskey.code.courses.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import whiskey.code.courses.config.properties.BotProperties;
import whiskey.code.courses.service.handler.MessageHandler;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoursesBot extends TelegramLongPollingBot {

    private final BotProperties botProperties;
    private final MessageHandler channelPostHandler;
    private final MessageHandler usualMessageHandler;
    private final MessageHandler callbackQueryHandler;

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            //В бот пришло сообщение из чатбота
            usualMessageHandler.handle(update, this);
        } else if (update.hasChannelPost()) {
            //В бот пришло сообщение из канала хранения уроков
            channelPostHandler.handle(update, this);
        } else if (update.hasCallbackQuery()) {
            //В бот пришло из инлайн клавиатуры
            callbackQueryHandler.handle(update,this );
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


