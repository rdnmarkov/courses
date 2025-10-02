package whiskey.code.courses.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import whiskey.code.courses.bot.CoursesBot;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BotInitializer {

    @Bean
    public TelegramBotsApi telegramBotsApi(CoursesBot coursesBot) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(coursesBot);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }

        return telegramBotsApi;
    }

}
