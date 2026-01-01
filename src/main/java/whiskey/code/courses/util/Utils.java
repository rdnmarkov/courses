package whiskey.code.courses.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import whiskey.code.courses.bot.CoursesBot;

import java.util.List;

import static whiskey.code.courses.util.Constants.*;

@Slf4j
@UtilityClass
public class Utils {

    public static ForwardMessage forwardMessage(Long toChatId, Long fromChatId, Integer messageId) {
        return ForwardMessage.builder()
                .chatId(String.valueOf(toChatId))
                .fromChatId(String.valueOf(fromChatId))
                .messageId(messageId)
                .protectContent(true)
                .build();
    }

    public static InlineKeyboardButton navButton(String text,
                                                 String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }

    public static DeleteMessage clearScreen(Long chatId, Integer messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId.toString());
        deleteMessage.setMessageId(messageId);

        return deleteMessage;
    }

    public static SendMessage messageSeparator(Long chatId, int percent) {

        int filledCount = (int) Math.round(TOTAL_LENGTH * percent / 100.0);
        filledCount = Math.max(0, Math.min(TOTAL_LENGTH, filledCount));

        StringBuilder progressBar = new StringBuilder();
        progressBar.append(FILLED_SYMBOL.repeat(filledCount));
        progressBar.append(EMPTY_SYMBOL.repeat(TOTAL_LENGTH - filledCount));

        String messageText = String.format(
                "<code>%s%s%d%%</code>",
                progressBar,
                DELIMITER,
                percent
        );

        SendMessage separator = new SendMessage();
        separator.setChatId(chatId.toString());
        separator.enableHtml(true);
        separator.setText(messageText);

        return separator;
    }

    public static InlineKeyboardButton createWebAppButton(Long chatId, String url) {

        String link = url + "?chatId=" + chatId;

        return InlineKeyboardButton.builder()
                .text("🌐 Открыть Web")
                .url(link)
                .build();
    }

    @Async
    public static void clearScreen(Long chatId, Integer messageId, CoursesBot bot) {

        try {
            bot.execute(Utils.clearScreen(chatId, messageId));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Async
    public static void sendMessage(SendMessage message, CoursesBot bot) {
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Async
    public static void updateMessage(EditMessageText message, CoursesBot bot) {
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Async
    public static void forwardMessage(ForwardMessage message, CoursesBot bot) {
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Async
    public ChatMember sendMemberReq(GetChatMember member, CoursesBot bot) {
        try {
            return bot.execute(member);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
