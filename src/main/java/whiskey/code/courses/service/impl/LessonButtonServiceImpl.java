package whiskey.code.courses.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import whiskey.code.courses.entity.Lesson;
import whiskey.code.courses.service.LessonButtonService;
import whiskey.code.courses.service.db.LessonService;

import java.util.ArrayList;
import java.util.List;

import static whiskey.code.courses.util.Constants.*;
import static whiskey.code.courses.util.Utils.navButton;


@Service
@RequiredArgsConstructor
public class LessonButtonServiceImpl implements LessonButtonService {

    private final LessonService lessonService;
    private final static String TEXT = "📚 Выберите урок:";
    private final static String BACK_TO_COURSE = "📚 Назад к курсам ◀";

    @Override
    public EditMessageText updateLessons(Long chatId, int pageLessons,
                                         int pageCourses, Long courseId,
                                         Integer messageId) {
        return EditMessageText.builder()
                .chatId(String.valueOf(chatId))
                .messageId(messageId)
                .text(TEXT)
                .replyMarkup(lessonsButtons(courseId, pageLessons, pageCourses))
                .build();
    }

    private InlineKeyboardMarkup lessonsButtons(Long courseId, int pageLessons, int pageCourses) {

        Page<Lesson> lessonPage = lessonService.findByPage(courseId, pageLessons);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        lessonPage.get().forEach(lesson -> {
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText(lesson.getTitle());
                    button.setCallbackData(LESSON + lesson.getId());
                    rows.add(List.of(button));
                }
        );

        List<InlineKeyboardButton> navButtons = new ArrayList<>();

        if (pageLessons > 0) navButtons.add(navButton(PREVIOUS,
                PAGE_LESSON + courseId + DELIMITER_PAGE + (pageLessons - 1)
                        + DELIMITER_PAGE + pageCourses));

        if (lessonPage.hasNext()) navButtons.add(navButton(NEXT,
                PAGE_LESSON + courseId + DELIMITER_PAGE + (pageLessons + 1)
                        + DELIMITER_PAGE + pageCourses));

        if (!navButtons.isEmpty()) {
            rows.add(navButtons);
        }

        rows.add(List.of(navButton(BACK_TO_COURSE, PAGE_COURSE + pageCourses)));

        markup.setKeyboard(rows);
        return markup;
    }
}
