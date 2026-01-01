package whiskey.code.courses.service.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.objects.Update;
import whiskey.code.courses.bot.CoursesBot;
import whiskey.code.courses.service.SubscribeService;
import whiskey.code.courses.service.UserStateService;
import whiskey.code.courses.service.db.CategoryService;
import whiskey.code.courses.service.db.CourseService;
import whiskey.code.courses.service.handler.UpdateHandler;
import whiskey.code.courses.service.impl.CategoryButtonServiceImpl;
import whiskey.code.courses.service.impl.CourseButtonServiceImpl;
import whiskey.code.courses.util.Utils;

@Service("messageHandler")
@RequiredArgsConstructor
public class MessageHandlerImpl implements UpdateHandler {

    private final CourseService courseService;
    private final SubscribeService subscribeService;
    private final CategoryButtonServiceImpl categoryButtonService;
    private final CourseButtonServiceImpl courseButtonService;
    private final UserStateService userStateService;

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

        if (userStateService.isWaitingForSearch(message.getChatId())) {
            userStateService.clearState(message.getChatId());
            Utils.sendMessage(courseButtonService.getSearchButtons(message, null), bot);
        } else {
            if (!subscribeService.isMember(Utils.sendMemberReq(member, bot))) {
                Utils.sendMessage(subscribeService.sendSubscriptionRequestMes(message.getChatId()), bot);
            } else {
                Utils.sendMessage(categoryButtonService.getButtons(message, null), bot);
            }
        }
    }
}
