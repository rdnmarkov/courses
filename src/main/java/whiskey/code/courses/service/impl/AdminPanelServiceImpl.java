package whiskey.code.courses.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import whiskey.code.courses.service.AdminPanelService;
import whiskey.code.courses.service.db.CourseService;
import whiskey.code.courses.service.db.LessonService;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static whiskey.code.courses.util.Constants.*;


@Service
@RequiredArgsConstructor
public class AdminPanelServiceImpl implements AdminPanelService {

    private final CourseService courseService;
    private final LessonService lessonService;

    private final Map<String, Function<String, String>> commandHandlers = new HashMap<>();

    @PostConstruct
    private void initCommandHandlers() {
        commandHandlers.put(CREATE_COURSE, text -> courseService.createCourse(stripCommand(text, CREATE_COURSE)).toString());
        commandHandlers.put(UPDATE_COURSE_FULL, text -> courseService.updateCourse(stripCommand(text, UPDATE_COURSE_FULL)).toString());
        commandHandlers.put(UPDATE_COURSE_TITLE, text -> courseService.updateCourseTitle(stripCommand(text, UPDATE_COURSE_TITLE)).toString());
        commandHandlers.put(UPDATE_COURSE_VISIBILITY, text -> courseService.updateCourseVis(stripCommand(text, UPDATE_COURSE_VISIBILITY)).toString());
        commandHandlers.put(DELETE_COURSE, text -> {
            courseService.deleteCourse(stripCommand(text, DELETE_COURSE));
            return "Курс удален!";
        });

        commandHandlers.put(CREATE_LESSON, text -> lessonService.createLesson(stripCommand(text, CREATE_LESSON)).toString());
        commandHandlers.put(UPDATE_LESSON_FULL, text -> lessonService.updateLesson(stripCommand(text, UPDATE_LESSON_FULL)).toString());
        commandHandlers.put(UPDATE_LESSON_TITLE, text -> lessonService.updateLessonTitle(stripCommand(text, UPDATE_LESSON_TITLE)).toString());
        commandHandlers.put(UPDATE_LESSON_COURSE, text -> lessonService.updateLessonCourse(stripCommand(text, UPDATE_LESSON_COURSE)).toString());
        commandHandlers.put(UPDATE_LESSON_MESSAGE, text -> lessonService.updateLessonMessage(stripCommand(text, UPDATE_LESSON_MESSAGE)).toString());
        commandHandlers.put(UPDATE_LESSON_ORDER, text -> lessonService.updateLessonOrderNumber(stripCommand(text, UPDATE_LESSON_ORDER)).toString());
        commandHandlers.put(DELETE_LESSON, text -> {
            lessonService.deleteLesson(stripCommand(text, DELETE_LESSON));
            return "Урок удален!";
        });
    }

    public SendMessage handleAdminCommand(Message message) {
        String text = message.getText();
        Long chatId = message.getChatId();

        String command = extractCommand(text);

        return new SendMessage(String.valueOf(chatId),
                commandHandlers.getOrDefault(command, someText -> String.valueOf(message.getMessageId())).apply(text));

    }

    private String extractCommand(String text) {
        if (text == null || text.length() < 4 || !text.startsWith("/")) {
            return "";
        }
        return text.substring(0, text.indexOf(' ') > 0 ? text.indexOf(' ') + 1 : text.length());
    }

    private String stripCommand(String text, String command) {
        return text.replace(command, "").trim();
    }
}
