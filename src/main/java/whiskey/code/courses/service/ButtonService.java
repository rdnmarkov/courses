package whiskey.code.courses.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface ButtonService {

    SendMessage buttonsCourses(Long chatId);
}
