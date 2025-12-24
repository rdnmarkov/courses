package whiskey.code.courses.service.handler.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import whiskey.code.courses.bot.CoursesBot;
import whiskey.code.courses.config.properties.BotProperties;
import whiskey.code.courses.service.SubscribeService;
import whiskey.code.courses.util.Utils;

@Service
@RequiredArgsConstructor
public class WebAppHandlerImpl {

    private final SubscribeService subscribeService;
    private final CoursesBot bot;
    private final BotProperties botProperties;


    public void handle(Long chatId, Integer messageId) {
        GetChatMember member = subscribeService.getMember(chatId);

        if (!subscribeService.isMember(Utils.sendMemberReq(member, bot))) {
            Utils.sendMessage(subscribeService.sendSubscriptionRequestMes(chatId), bot);
        } else {

            Utils.forwardMessage(Utils.forwardMessage(chatId,
                    botProperties.getAdminChannel(),
                    messageId), bot);
        }
    }
}
