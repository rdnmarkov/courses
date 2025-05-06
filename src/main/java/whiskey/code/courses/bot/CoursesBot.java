package whiskey.code.courses.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import whiskey.code.courses.config.properties.BotProperties;
import whiskey.code.courses.service.AdminPanelService;
import whiskey.code.courses.service.db.LessonService;
import whiskey.code.courses.service.impl.CourseButtonServiceImpl;
import whiskey.code.courses.service.impl.LessonButtonServiceImpl;
import whiskey.code.courses.util.Utils;

import static whiskey.code.courses.util.Constants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoursesBot extends TelegramLongPollingBot {

    private final BotProperties botProperties;
    private final CourseButtonServiceImpl courseButtonService;
    private final LessonButtonServiceImpl lessonButtonService;
    private final LessonService lessonService;
    private final AdminPanelService adminPanelService;

    @Override
    public void onUpdateReceived(Update update) {
        //В бот пришло первое сообщение из чатбота
        if (update.hasMessage()) {
            var message = update.getMessage();

            clearScreen(message);
            sendMessage(courseButtonService.getButtons(message, null));


        } else if (update.hasChannelPost()) {
            //В бот пришло сообщение из канала хранения уроков
            var chatId = update.getChannelPost().getChatId();
            Message channelPost = update.getChannelPost();

            if (chatId.equals(botProperties.getAdminChannel())) {
                sendMessage(adminPanelService.handleAdminCommand(channelPost));
            }

        } else if (update.hasCallbackQuery()) {
            //В бот пришло из инлайн клавиатуры

            var callbackQuery = update.getCallbackQuery();
            var callbackData = update.getCallbackQuery().getData();

            if (callbackData.startsWith(PAGE_COURSE)) {
                updateMessage(courseButtonService.updateButtons(callbackQuery));
            } else if (callbackData.startsWith(PAGE_LESSON)) {
                updateMessage(lessonButtonService.updateButtons(callbackQuery));
            } else if (callbackData.startsWith(LESSON)) {
                //пересылка уроков

                var lessonId = Long.parseLong(callbackQuery.getData().split("_")[1]);

                var chatId = callbackQuery.getMessage().getChatId();
                var lesson = lessonService.findLesson(lessonId);

                lesson.getMessageIds().forEach(messageId ->
                                forwardMessage(Utils.forwardMessage(chatId,
                                        botProperties.getAdminChannel(),
                                        messageId)));

                sendMessage(lessonButtonService.getButtons(null, callbackQuery));
            }
        }
    }

    private void clearScreen(Message message){

        Long chatId = message.getChatId();
        Integer messageId = message.getMessageId();

        try {
            execute(Utils.clearScreen(chatId, messageId));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMessage(SendMessage message){
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateMessage(EditMessageText message){
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void forwardMessage(ForwardMessage message){
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String getBotUsername() {
        return botProperties.getName();
    }

    @Override
    public String getBotToken() {
        return botProperties.getKey();
    }
}
