package whiskey.code.courses.service;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public interface LessonButtonService {

    EditMessageText updateLessons(Long chatId, int pageLessons, int pageCourses, Long courseId, Integer messageId);

}
