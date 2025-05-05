package whiskey.code.courses.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public interface LessonButtonService {

    EditMessageText updateLessons(Long chatId, int page, Long courseId, Integer messageId);

}
