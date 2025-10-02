package whiskey.code.courses.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import whiskey.code.courses.config.properties.BotProperties;
import whiskey.code.courses.service.handler.RouterHandler;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoursesBot extends TelegramLongPollingBot {

    private final BotProperties botProperties;
    private final RouterHandler handle;

    @Override
    public void onUpdateReceived(Update update) {
        CompletableFuture.runAsync(() -> {
            handle.route(update, this);
        });
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


