package whiskey.code.courses.service.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import whiskey.code.courses.bot.CoursesBot;
import whiskey.code.courses.config.properties.BotProperties;
import whiskey.code.courses.service.SubscribeService;
import whiskey.code.courses.service.TelegramService;
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
    private final TelegramService telegramService;

    @Override
    public boolean canHandle(Update update) {
        return update.hasCallbackQuery();
    }

    @Override
    public void handle(Update update, CoursesBot bot) {
        var callbackQuery = update.getCallbackQuery();
        var callbackData = callbackQuery.getData();
        Message message = callbackQuery.getMessage();

        var member = subscribeService.getMember(message.getFrom().getId());

        if (!subscribeService.isMember(telegramService.sendMemberReq(member,bot))) {
            telegramService.sendMessage(subscribeService.sendSubscriptionRequestMes(message.getChatId()), bot);
        } else {
            if (callbackData.startsWith(PAGE_SEARCH)) {
                SendMessage sm = new SendMessage();
                sm.setChatId(message.getChatId());
                sm.setText("🔎 Введите текст для поиска:");
                userStateService.setState(message.getChatId());
                telegramService.sendMessage(sm, bot);
            } else if (callbackData.startsWith(PAGE_CATEGORY)) {
                telegramService.updateMessage(categoryButtonService.updateButtons(callbackQuery), bot);
            } else if (callbackData.startsWith(PAGE_COURSE)) {
                telegramService.updateMessage(courseButtonService.updateButtons(callbackQuery), bot);
            } else if (callbackData.startsWith(PAGE_LESSON)) {
                telegramService.updateMessage(lessonButtonService.updateButtons(callbackQuery), bot);
            } else if (callbackData.startsWith(LESSON)) {
                sendLessons(message, callbackQuery, bot);
            } else if (callbackData.startsWith(PAGE_COURSE_SEARCH)) {
                telegramService.updateMessage(courseButtonService.updateSearchButtons(callbackQuery), bot);
            } else if (callbackData.startsWith(PAGE_LESSON_SEARCH)) {
                telegramService.updateMessage(lessonButtonService.updateSearchButtons(callbackQuery), bot);
            } else if (callbackData.startsWith(LESSON_SEARCH)) {
                sendLessons(message, callbackQuery, bot);
            }
        }
    }

    private void sendLessons(Message message, CallbackQuery callbackQuery, CoursesBot bot) {
       telegramService.deleteMessage(message.getChatId(), message.getMessageId(), bot);

        var lessonId = Long.parseLong(callbackQuery.getData().split("_")[1]);
        var chatId = message.getChatId();
        var lesson = lessonService.findLesson(lessonId);

        lesson.getMessageIds().forEach(messageId ->
                telegramService.forwardMessage(Utils.forwardMessage(chatId,
                        botProperties.getAdminChannel(),
                        messageId), bot));

        telegramService.sendMessage(Utils.messageSeparator(chatId,
                lessonService.getPercent(callbackQuery, lesson.getOrderNumber())), bot);

        SendMessage lessonsButtons;

        if (callbackQuery.getData().startsWith(LESSON)) {
            lessonsButtons = lessonButtonService.getButtons(null, callbackQuery);
        } else {
            lessonsButtons = lessonButtonService.getSearchButtons(null, callbackQuery);
        }

        telegramService.sendMessage(lessonsButtons, bot);
    }
}
