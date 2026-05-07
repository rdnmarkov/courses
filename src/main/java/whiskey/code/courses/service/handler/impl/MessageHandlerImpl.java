package whiskey.code.courses.service.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.objects.Update;
import whiskey.code.courses.bot.CoursesBot;
import whiskey.code.courses.service.SubscribeService;
import whiskey.code.courses.service.TelegramService;
import whiskey.code.courses.service.UserStateService;
import whiskey.code.courses.service.db.CourseService;
import whiskey.code.courses.service.handler.UpdateHandler;
import whiskey.code.courses.service.impl.CategoryButtonServiceImpl;
import whiskey.code.courses.service.impl.CourseButtonServiceImpl;

@Service("messageHandler")
@RequiredArgsConstructor
public class MessageHandlerImpl implements UpdateHandler {

    private final CourseService courseService;
    private final SubscribeService subscribeService;
    private final CategoryButtonServiceImpl categoryButtonService;
    private final CourseButtonServiceImpl courseButtonService;
    private final UserStateService userStateService;
    private final TelegramService telegramService;

    @Override
    public boolean canHandle(Update update) {
        return update.hasMessage();
    }

    @Override
    public void handle(Update update, CoursesBot bot) {
        var message = update.getMessage();

        telegramService.deleteMessage(message.getChatId(), message.getMessageId(), bot);
        GetChatMember member = subscribeService.getMember(message.getFrom().getId());

        if (userStateService.isWaitingForSearch(message.getChatId())) {
            userStateService.clearState(message.getChatId());
            telegramService.sendMessage(courseButtonService.getSearchButtons(message, null), bot);
        } else {
            if (!subscribeService.isMember(telegramService.sendMemberReq(member, bot))) {
                telegramService.sendMessage(subscribeService.sendSubscriptionRequestMes(message.getChatId()), bot);
            } else {
                telegramService.sendMessage(categoryButtonService.getButtons(message, null), bot);
            }
        }
    }
}
