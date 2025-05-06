package whiskey.code.courses.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import whiskey.code.courses.entity.Lesson;
import whiskey.code.courses.service.ButtonService;
import whiskey.code.courses.service.db.LessonService;

import java.util.ArrayList;
import java.util.List;

import static whiskey.code.courses.util.Constants.*;
import static whiskey.code.courses.util.Utils.navButton;


@Service
@RequiredArgsConstructor
public class LessonButtonServiceImpl implements ButtonService {

    private final LessonService lessonService;
    private final static String TEXT = "\uD83C\uDF44 Выберите урок:";
    private final static String BACK_TO_COURSE = "\uD83C\uDFF0 Назад к курсам \uD83D\uDD19";

    @Override
    public EditMessageText updateButtons(CallbackQuery callbackQuery) {

        Integer messageId = callbackQuery.getMessage().getMessageId();
        Long chatId = callbackQuery.getMessage().getChatId();

        String[] lessonsInfo = callbackQuery.getData().split("_");
        long courseId = Long.parseLong(lessonsInfo[1]);
        int pageLessons = Integer.parseInt(lessonsInfo[2]);
        int pageCourses = Integer.parseInt(lessonsInfo[3]);

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
                    button.setCallbackData(LESSON + lesson.getId() +
                            DELIMITER_PAGE + courseId +
                            DELIMITER_PAGE + pageLessons +
                            DELIMITER_PAGE + pageCourses);
                    rows.add(List.of(button));
                }
        );

        List<InlineKeyboardButton> navButtons = new ArrayList<>();

        if (pageLessons > 0) navButtons.add(navButton(PREVIOUS,
                PAGE_LESSON + courseId +
                        DELIMITER_PAGE + (pageLessons - 1) +
                        DELIMITER_PAGE + pageCourses));

        if (lessonPage.hasNext()) navButtons.add(navButton(NEXT,
                PAGE_LESSON + courseId +
                        DELIMITER_PAGE + (pageLessons + 1) +
                        DELIMITER_PAGE + pageCourses));

        if (!navButtons.isEmpty()) {
            rows.add(navButtons);
        }

        rows.add(List.of(navButton(BACK_TO_COURSE, PAGE_COURSE + pageCourses)));

        markup.setKeyboard(rows);
        return markup;
    }

    @Override
    public SendMessage getButtons(Message message, CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();

        String[] lessonsInfo = callbackQuery.getData().split("_");
        long courseId = Long.parseLong(lessonsInfo[2]);
        int pageLessons = Integer.parseInt(lessonsInfo[3]);
        int pageCourses = Integer.parseInt(lessonsInfo[4]);

        return SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(TEXT)
                .replyMarkup(lessonsButtons(courseId, pageLessons, pageCourses)).build();
    }
}
