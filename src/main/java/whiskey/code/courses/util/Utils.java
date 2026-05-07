package whiskey.code.courses.util;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import static whiskey.code.courses.util.Constants.*;

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

    public static InlineKeyboardButton navButton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
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
                .text(WEB_PAGE)
                .url(link)
                .build();
    }
}
