package whiskey.code.courses.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public interface ButtonService {

    SendMessage courses(Long chatId, int page );

    EditMessageText updateCourses(Long chatId, int page, Integer messageId);
}
