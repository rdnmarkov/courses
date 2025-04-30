package whiskey.code.courses.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import whiskey.code.courses.config.properties.BotProperties;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoursesBot extends TelegramLongPollingBot {

    private final BotProperties botProperties;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Long chatId = update.getMessage().getChatId();

            ForwardMessage message = getForwardMessage(chatId, 17);

            try {
                execute(message);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

        } else if (update.hasChannelPost()) {
            Long chatId = update.getChannelPost().getChatId();
            if (chatId.equals(botProperties.getAdminChannel())) {
                log.info("catId - " + chatId + " messageId - " + update.getChannelPost().getMessageId());
            }
        }
    }

    private ForwardMessage getForwardMessage(Long chatId, Integer messageId) {
        ForwardMessage message = ForwardMessage.builder()
                .chatId(String.valueOf(chatId))
                .fromChatId(String.valueOf(botProperties.getAdminChannel()))
                .messageId(messageId)
                .protectContent(true)
                .build();
        return message;
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
