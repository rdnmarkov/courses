package whiskey.code.courses.service.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import whiskey.code.courses.bot.CoursesBot;
import whiskey.code.courses.config.properties.BotProperties;
import whiskey.code.courses.service.SubscribeService;
import whiskey.code.courses.service.UserStateService;
import whiskey.code.courses.service.db.LessonService;
import whiskey.code.courses.service.handler.UpdateHandler;
import whiskey.code.courses.service.impl.CategoryButtonServiceImpl;
import whiskey.code.courses.service.impl.CourseButtonServiceImpl;
import whiskey.code.courses.service.impl.LessonButtonServiceImpl;
import whiskey.code.courses.util.Utils;

import static whiskey.code.courses.util.Constants.*;

@Service("callbackQueryHandler")
@RequiredArgsConstructor
public class CallbackQueryHandlerImpl implements UpdateHandler {

    private final BotProperties botProperties;
    private final CourseButtonServiceImpl courseButtonService;
    private final LessonButtonServiceImpl lessonButtonService;
    private final CategoryButtonServiceImpl categoryButtonService;
    private final LessonService lessonService;
    private final SubscribeService subscribeService;
    private final UserStateService userStateService;

    @Override
    public boolean canHandle(Update update) {
        return update.hasCallbackQuery();
    }

    @Override
    public void handle(Update update, CoursesBot bot) {
        //В бот пришло из инлайн клавиатуры
        var callbackQuery = update.getCallbackQuery();
        var callbackData = callbackQuery.getData();
        Message message = callbackQuery.getMessage();

        GetChatMember member = subscribeService.getMember(message.getFrom().getId());

        if (!subscribeService.isMember(Utils.sendMemberReq(member, bot))) {
            Utils.sendMessage(subscribeService.sendSubscriptionRequestMes(message.getChatId()), bot);
        } else {
            if (callbackData.startsWith("SEARCH")) {
                SendMessage sm = new SendMessage();
                sm.setChatId(message.getChatId());
                sm.setText("🔎 Введите текст для поиска:");
                userStateService.setState(message.getChatId());
                Utils.sendMessage(sm, bot);
            } else if (callbackData.startsWith(PAGE_CATEGORY)) {
                Utils.updateMessage(categoryButtonService.updateButtons(callbackQuery), bot);
            } else if (callbackData.startsWith(PAGE_COURSE)) {
                Utils.updateMessage(courseButtonService.updateButtons(callbackQuery), bot);
            } else if (callbackData.startsWith(PAGE_LESSON)) {
                Utils.updateMessage(lessonButtonService.updateButtons(callbackQuery), bot);
            } else if (callbackData.startsWith(LESSON)) {
                sendLessons(bot, message, callbackQuery);
            } else if (callbackData.startsWith(PAGE_COURSE_SEARCH)) {
                Utils.updateMessage(courseButtonService.updateSearchButtons(callbackQuery), bot);
            } else if (callbackData.startsWith(PAGE_LESSON_SEARCH)) {
                Utils.updateMessage(lessonButtonService.updateSearchButtons(callbackQuery), bot);
            } else if (callbackData.startsWith(LESSON_SEARCH)) {
                sendLessons(bot, message, callbackQuery);
            }
        }
    }

    private void sendLessons(CoursesBot bot, Message message, CallbackQuery callbackQuery) {
        //пересылка уроков
        Utils.clearScreen(message.getChatId(), message.getMessageId(), bot);

        var lessonId = Long.parseLong(callbackQuery.getData().split("_")[1]);

        var chatId = message.getChatId();
        var lesson = lessonService.findLesson(lessonId);

        lesson.getMessageIds().forEach(messageId ->
                Utils.forwardMessage(Utils.forwardMessage(chatId,
                        botProperties.getAdminChannel(),
                        messageId), bot));

        Utils.sendMessage(Utils.messageSeparator(chatId,
                lessonService.getPercent(callbackQuery, lesson.getOrderNumber())), bot);

        SendMessage lessonsButtons;

        if (callbackQuery.getData().startsWith(LESSON)) {
            lessonsButtons = lessonButtonService.getButtons(null, callbackQuery);
        } else {
            lessonsButtons = lessonButtonService.getSearchButtons(null, callbackQuery);
        }

        Utils.sendMessage(lessonsButtons, bot);
    }
}
