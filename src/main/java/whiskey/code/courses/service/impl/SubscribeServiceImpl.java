package whiskey.code.courses.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import whiskey.code.courses.config.properties.BotProperties;
import whiskey.code.courses.service.SubscribeService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscribeServiceImpl implements SubscribeService {

    private final BotProperties properties;
    private static final String MEMBER_ROLE = "member";
    private static final String MEMBER_ADMINISTRATOR = "administrator";
    private static final String MEMBER_CREATOR = "creator";
    private static final String TEXT = "Для использования бота необходимо подписаться на наш канал!";
    private static final String SUBSCRIBE = "Подписаться на канал";

    @Override
    public GetChatMember getMember(Long userId) {
        return new GetChatMember(properties.getSubChannel(), userId);
    }

    @Override
    public boolean isMember(ChatMember member) {
        return MEMBER_ROLE.equals(member.getStatus()) ||
                MEMBER_ADMINISTRATOR.equals(member.getStatus()) ||
                MEMBER_CREATOR.equals(member.getStatus());
    }

    @Override
    public SendMessage sendSubscriptionRequestMes(Long chatId) {
        String channelUsername = properties.getSubChannel();
        String link = buildChannelLink(channelUsername);

        SendMessage message = new SendMessage(chatId.toString(), TEXT);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(SUBSCRIBE);
        button.setUrl(link);

        markup.setKeyboard(List.of(List.of(button)));
        message.setReplyMarkup(markup);

        return message;
    }

    private String buildChannelLink(String channelUsername) {
        String username = channelUsername.startsWith("@")
                ? channelUsername.substring(1)
                : channelUsername;
        return "https://t.me/" + username;
    }
}
