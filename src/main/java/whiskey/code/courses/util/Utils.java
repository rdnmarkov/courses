package whiskey.code.courses.util;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

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



}
