package whiskey.code.courses.service;

import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

public interface SubscribeService {

    GetChatMember getMember(Long userId);

    boolean isMember(ChatMember member);

    SendMessage sendSubscriptionRequestMes(Long chatId);
}
