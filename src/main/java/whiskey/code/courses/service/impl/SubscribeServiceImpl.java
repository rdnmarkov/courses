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
    private final static String MEMBER_ROLE = "member";
    private final static String MEMBER_ADMINISTRATOR = "administrator";
    private final static String MEMBER_CREATOR = "creator";
    private final static String TEXT = "Для использования бота необходимо подписаться на наш канал!";
    private final static String SUBSCRIBE = "Подписаться на канал";
    private final static String LINK = "https://t.me/whiskeycode";


    @Override
    public GetChatMember getMember(Long userId) {

        return new GetChatMember(properties.getSubChannel(), userId);
    }

    @Override
    public boolean isMember(ChatMember member) {

        return member.getStatus().equals(MEMBER_ROLE) ||
                member.getStatus().equals(MEMBER_ADMINISTRATOR) ||
                member.getStatus().equals(MEMBER_CREATOR);
    }

    @Override
    public SendMessage sendSubscriptionRequestMes(Long chatId) {

        SendMessage message = new SendMessage(chatId.toString(), TEXT);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(SUBSCRIBE);
        button.setUrl(LINK);
        button.setCallbackData("/clear");

        markup.setKeyboard(List.of(List.of(button)));
        message.setReplyMarkup(markup);

        return message;
    }


}
