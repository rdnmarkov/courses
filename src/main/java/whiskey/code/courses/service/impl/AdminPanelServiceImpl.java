package whiskey.code.courses.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import whiskey.code.courses.service.AdminPanelService;
import whiskey.code.courses.service.db.CourseService;
import whiskey.code.courses.service.db.LessonService;

import static whiskey.code.courses.util.Constants.*;


@Service
@RequiredArgsConstructor
public class AdminPanelServiceImpl implements AdminPanelService {

    private final CourseService courseService;
    private final LessonService lessonService;

    public SendMessage handleAdminCommand(Message message) {
        String text = message.getText();
        Long chatId = message.getChatId();

        String command = extractCommand(text);

        return switch (command) {
            case CREATE_COURSE -> new SendMessage(String.valueOf(chatId),
                    courseService.createCourse(text.replace(CREATE_COURSE, "")).toString());

            case UPDATE_COURSE_FULL -> new SendMessage(String.valueOf(chatId),
                    courseService.updateCourse(text.replace(UPDATE_COURSE_FULL, "")).toString());

            case UPDATE_COURSE_TITLE -> new SendMessage(String.valueOf(chatId),
                    courseService.updateCourseTitle(text.replace(UPDATE_COURSE_TITLE, "")).toString());

            case UPDATE_COURSE_VISIBILITY -> new SendMessage(String.valueOf(chatId),
                    courseService.updateCourseVis(text.replace(UPDATE_COURSE_VISIBILITY, "")).toString());

            case DELETE_COURSE -> {
                courseService.deleteCourse(text.replace(DELETE_COURSE, ""));
                yield new SendMessage(String.valueOf(chatId), "Курс удален!");
            }

            case CREATE_LESSON -> new SendMessage(String.valueOf(chatId),
                    lessonService.createLesson(text.replace(CREATE_LESSON, "")).toString());

            case UPDATE_LESSON_FULL -> new SendMessage(String.valueOf(chatId),
                    lessonService.updateLesson(text.replace(UPDATE_LESSON_FULL, "")).toString());

            case UPDATE_LESSON_TITLE -> new SendMessage(String.valueOf(chatId),
                    lessonService.updateLessonTitle(text.replace(UPDATE_LESSON_TITLE, "")).toString());

            case UPDATE_LESSON_COURSE -> new SendMessage(String.valueOf(chatId),
                    lessonService.updateLessonCourse(text.replace(UPDATE_LESSON_COURSE, "")).toString());

            case UPDATE_LESSON_MESSAGE -> new SendMessage(String.valueOf(chatId),
                    lessonService.updateLessonMessage(text.replace(UPDATE_LESSON_MESSAGE, "")).toString());

            case UPDATE_LESSON_ORDER -> new SendMessage(String.valueOf(chatId),
                    lessonService.updateLessonOrderNumber(text.replace(UPDATE_LESSON_ORDER, "")).toString());

            case DELETE_LESSON -> {
                lessonService.deleteLesson(text.replace(DELETE_LESSON, ""));
                yield new SendMessage(String.valueOf(chatId), "Урок удален!");
            }

            default -> new SendMessage(String.valueOf(chatId), String.valueOf(message.getMessageId()));
        };
    }

    private String extractCommand(String text) {
        if (text == null || text.length() < 4 || !text.startsWith("/")) {
            return "";
        }
        return text.substring(0, text.indexOf(' ') > 0 ? text.indexOf(' ') + 1 : text.length());
    }
}
