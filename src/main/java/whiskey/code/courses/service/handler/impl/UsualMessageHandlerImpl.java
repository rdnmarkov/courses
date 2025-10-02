package whiskey.code.courses.service.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.objects.Update;
import whiskey.code.courses.bot.CoursesBot;
import whiskey.code.courses.service.SubscribeService;
import whiskey.code.courses.service.handler.MessageHandler;
import whiskey.code.courses.service.impl.CourseButtonServiceImpl;
import whiskey.code.courses.util.Utils;

@Service("usualMessageHandler")
@RequiredArgsConstructor
public class UsualMessageHandlerImpl implements MessageHandler {

    private final CourseButtonServiceImpl courseButtonService;
    private final SubscribeService subscribeService;

    @Override
    public boolean canHandle(Update update) {
        return update.hasMessage();
    }

    @Override
    public void handle(Update update, CoursesBot bot) {
        //В бот пришло сообщение из чатбота
        var message = update.getMessage();

        Utils.clearScreen(message.getChatId(), message.getMessageId(), bot);
        GetChatMember member = subscribeService.getMember(message.getFrom().getId());

        if (!subscribeService.isMember(Utils.sendMemberReq(member, bot))) {
            Utils.sendMessage(subscribeService.sendSubscriptionRequestMes(message.getChatId()), bot);
        } else {
            Utils.sendMessage(courseButtonService.getButtons(message, null), bot);
        }
    }
}
