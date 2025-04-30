package whiskey.code.courses.util;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;

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
}
