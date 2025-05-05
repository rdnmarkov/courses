package whiskey.code.courses.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import whiskey.code.courses.config.properties.BotProperties;
import whiskey.code.courses.service.ButtonService;
import whiskey.code.courses.service.impl.CourseButtonServiceImpl;
import whiskey.code.courses.service.impl.LessonButtonServiceImpl;
import whiskey.code.courses.util.Utils;

import static whiskey.code.courses.util.Constants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoursesBot extends TelegramLongPollingBot {

    private final BotProperties botProperties;
    private final CourseButtonServiceImpl courses;
    private final LessonButtonServiceImpl lessons;

    @Override
    public void onUpdateReceived(Update update) {
        //В бот пришло первое сообщение из чатбота
        if (update.hasMessage()) {
            Message message = update.getMessage();

            clearScreen(message);
            sendMessage(courses.getButtons(message));


        } else if (update.hasChannelPost()) {
            //В бот пришло сообщение из канала храрения уроков
            var chatId = update.getChannelPost().getChatId();

            if (chatId.equals(botProperties.getAdminChannel())) {
                sendMessage(new SendMessage(String.valueOf(chatId),
                        String.valueOf(update.getChannelPost().getMessageId())));

            }
        } else if (update.hasCallbackQuery()) {
            //В бот пришло из инлайн клавиатуры

            CallbackQuery callbackQuery = update.getCallbackQuery();
            var callbackData = update.getCallbackQuery().getData();

            if (callbackData.startsWith(PAGE_COURSE)) {
                updateMessage(courses.updateButtons(callbackQuery));
            } else if (callbackData.startsWith(PAGE_LESSON)) {
                updateMessage(lessons.updateButtons(callbackQuery));
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


    @Override
    public String getBotUsername() {
        return botProperties.getName();
    }

    @Override
    public String getBotToken() {
        return botProperties.getKey();
    }
}
