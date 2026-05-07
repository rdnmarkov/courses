package whiskey.code.courses.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import whiskey.code.courses.bot.CoursesBot;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramService {

    @Async
    public void sendMessage(SendMessage message, CoursesBot coursesBot) {
        try {
            coursesBot.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Async
    public void updateMessage(EditMessageText message, CoursesBot coursesBot) {
        try {
            coursesBot.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Async
    public void forwardMessage(ForwardMessage message, CoursesBot coursesBot) {
        try {
            coursesBot.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Async
    public void deleteMessage(Long chatId, Integer messageId, CoursesBot coursesBot) {
        try {
            coursesBot.execute(new DeleteMessage(String.valueOf(chatId), messageId));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    public ChatMember sendMemberReq(GetChatMember member, CoursesBot coursesBot) {
        try {
            return coursesBot.execute(member);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
