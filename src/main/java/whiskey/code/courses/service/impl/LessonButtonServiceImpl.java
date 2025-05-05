package whiskey.code.courses.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import whiskey.code.courses.entity.Lesson;
import whiskey.code.courses.service.LessonButtonService;
import whiskey.code.courses.service.db.LessonService;

import java.util.ArrayList;
import java.util.List;

import static whiskey.code.courses.util.Constants.PREVIOUS;
import static whiskey.code.courses.util.Constants.NEXT;
import static whiskey.code.courses.util.Constants.LESSON;
import static whiskey.code.courses.util.Constants.PAGE_LESSON;


@Service
@RequiredArgsConstructor
public class LessonButtonServiceImpl implements LessonButtonService {

    private final LessonService lessonService;
    private final static String TEXT = "📚 Выберите урок:";

    @Override
    public EditMessageText updateLessons(Long chatId, int page, Long courseId, Integer messageId) {
        return EditMessageText.builder()
                .chatId(String.valueOf(chatId))
                .messageId(messageId)
                .text(TEXT)
                .replyMarkup(lessonsButtons(courseId, page))
                .build();
    }

    private InlineKeyboardMarkup lessonsButtons(Long courseId, int page) {

        Page<Lesson> lessonPage = lessonService.findByPage(courseId, page);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> navButtons = new ArrayList<>();

        if (page > 0) {
            InlineKeyboardButton prevButton = new InlineKeyboardButton();
            prevButton.setText(PREVIOUS);
            prevButton.setCallbackData(PAGE_LESSON + courseId + "_" + (page - 1));
            navButtons.add(prevButton);
        }

        lessonPage.get().forEach(lesson -> {
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText(lesson.getTitle());
                    button.setCallbackData(LESSON + lesson.getId());
                    rows.add(List.of(button));
                }
        );

        if (lessonPage.hasNext()) {
            InlineKeyboardButton nextButton = new InlineKeyboardButton();
            nextButton.setText(NEXT);
            nextButton.setCallbackData(PAGE_LESSON + courseId + "_" + (page + 1));
            navButtons.add(nextButton);
        }

        if (!navButtons.isEmpty()) {
            rows.add(navButtons);
        }

        markup.setKeyboard(rows);
        return markup;
    }
}
