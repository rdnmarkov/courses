package whiskey.code.courses.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import whiskey.code.courses.config.properties.BotProperties;
import whiskey.code.courses.service.ButtonService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoursesBot extends TelegramLongPollingBot {

    private final BotProperties botProperties;
    private final ButtonService buttonService;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            var chatId = update.getMessage().getChatId();
            try {
                execute(buttonService.courses(chatId, 0));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

        } else if (update.hasChannelPost()) {
            var chatId = update.getChannelPost().getChatId();
            if (chatId.equals(botProperties.getAdminChannel())) {

                SendMessage message = new SendMessage(String.valueOf(chatId),
                        String.valueOf(update.getChannelPost().getMessageId()));

                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }

            }
        }else if (update.hasCallbackQuery()) {
            var callbackData = update.getCallbackQuery().getData();
            var chatId = update.getCallbackQuery().getMessage().getChatId();

            if (callbackData.startsWith("page_")) {
                int page = Integer.parseInt(callbackData.split("_")[1]);
                try {
                    execute(buttonService.updateCourses(chatId, page,
                            update.getCallbackQuery().getMessage().getMessageId()));
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
