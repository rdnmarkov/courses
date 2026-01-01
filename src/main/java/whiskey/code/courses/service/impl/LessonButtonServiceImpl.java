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
import whiskey.code.courses.config.properties.BotProperties;
import whiskey.code.courses.entity.Lesson;
import whiskey.code.courses.service.ButtonSearchService;
import whiskey.code.courses.service.ButtonService;
import whiskey.code.courses.service.db.LessonService;

import java.util.ArrayList;
import java.util.List;

import static whiskey.code.courses.util.Constants.*;
import static whiskey.code.courses.util.Utils.createWebAppButton;
import static whiskey.code.courses.util.Utils.navButton;


@Service
@RequiredArgsConstructor
public class LessonButtonServiceImpl implements ButtonService, ButtonSearchService {

    private final LessonService lessonService;
    private final static String TEXT = "\uD83C\uDF44 Выберите урок:";
    private final static String BACK_TO_COURSE = "\uD83C\uDFF0 Назад к курсам \uD83D\uDD19";
    private final static String BACK_TO_SEARCH_COURSE = "🔎 Назад к найденым курсам \uD83D\uDD19";
    private final BotProperties botProperties;

    @Override
    public EditMessageText updateButtons(CallbackQuery callbackQuery) {

        Integer messageId = callbackQuery.getMessage().getMessageId();
        Long chatId = callbackQuery.getMessage().getChatId();

        String[] lessonsInfo = callbackQuery.getData().split("_");
        long courseId = Long.parseLong(lessonsInfo[1]);
        int pageLessons = Integer.parseInt(lessonsInfo[2]);
        int pageCourses = Integer.parseInt(lessonsInfo[3]);
        int pageCategory = Integer.parseInt(lessonsInfo[4]);
        long categoryId = Long.parseLong(lessonsInfo[5]);

        return EditMessageText.builder()
                .chatId(String.valueOf(chatId))
                .messageId(messageId)
                .text(TEXT)
                .replyMarkup(lessonsButtons(courseId, categoryId, pageLessons, pageCourses, pageCategory, chatId))
                .build();
    }

    @Override
    public SendMessage getButtons(Message message, CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();

        String[] lessonsInfo = callbackQuery.getData().split("_");
        long courseId = Long.parseLong(lessonsInfo[2]);
        int pageLessons = Integer.parseInt(lessonsInfo[3]);
        int pageCourses = Integer.parseInt(lessonsInfo[4]);
        int pageCategory = Integer.parseInt(lessonsInfo[5]);
        long categoryId = Long.parseLong(lessonsInfo[6]);

        return SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(TEXT)
                .replyMarkup(lessonsButtons(courseId, categoryId, pageLessons, pageCourses, pageCategory, chatId)).build();
    }

    @Override
    public SendMessage getSearchButtons(Message message, CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();

        String[] lessonsSearchInfo = callbackQuery.getData().split("_");
        long courseId = Long.parseLong(lessonsSearchInfo[2]);
        int pageLessons = Integer.parseInt(lessonsSearchInfo[3]);
        int pageCourses = Integer.parseInt(lessonsSearchInfo[4]);

        return SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(TEXT)
                .replyMarkup(lessonsSearchButtons(courseId, pageLessons, pageCourses, lessonsSearchInfo[5], chatId)).build();
    }

    @Override
    public EditMessageText updateSearchButtons(CallbackQuery callbackQuery) {
        Integer messageId = callbackQuery.getMessage().getMessageId();
        Long chatId = callbackQuery.getMessage().getChatId();

        String[] lessonsSearchInfo = callbackQuery.getData().split("_");
        long courseId = Long.parseLong(lessonsSearchInfo[1]);
        int pageLessons = Integer.parseInt(lessonsSearchInfo[2]);
        int pageCourses = Integer.parseInt(lessonsSearchInfo[3]);

        return EditMessageText.builder()
                .chatId(String.valueOf(chatId))
                .messageId(messageId)
                .text(TEXT)
                .replyMarkup(lessonsSearchButtons(courseId, pageLessons, pageCourses, lessonsSearchInfo[4], chatId))
                .build();
    }

    private InlineKeyboardMarkup lessonsButtons(Long courseId, Long categoryId, int pageLessons, int pageCourses, int pageCategory, Long chatId) {

        Page<Lesson> lessonPage = lessonService.findByPage(courseId, pageLessons);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        lessonPage.get().forEach(lesson -> {
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText(lesson.getTitle());
                    button.setCallbackData(LESSON + lesson.getId() +
                            DELIMITER_PAGE + courseId +
                            DELIMITER_PAGE + pageLessons +
                            DELIMITER_PAGE + pageCourses +
                            DELIMITER_PAGE + pageCategory +
                            DELIMITER_PAGE + categoryId
                    );
                    rows.add(List.of(button));
                }
        );

        List<InlineKeyboardButton> navButtons = new ArrayList<>();

        if (pageLessons > 0) navButtons.add(navButton(PREVIOUS,
                PAGE_LESSON + courseId +
                        DELIMITER_PAGE + (pageLessons - 1) +
                        DELIMITER_PAGE + pageCourses +
                        DELIMITER_PAGE + pageCategory +
                        DELIMITER_PAGE + categoryId
        ));

        if (lessonPage.hasNext()) navButtons.add(navButton(NEXT,
                PAGE_LESSON + courseId +
                        DELIMITER_PAGE + (pageLessons + 1) +
                        DELIMITER_PAGE + pageCourses +
                        DELIMITER_PAGE + pageCategory +
                        DELIMITER_PAGE + categoryId
        ));

        if (!navButtons.isEmpty()) {
            rows.add(navButtons);
        }

        rows.add(List.of(navButton(BACK_TO_COURSE, PAGE_COURSE + categoryId +
                DELIMITER_PAGE + pageCourses +
                DELIMITER_PAGE + pageCategory)));
        rows.add(List.of(navButton(BACK_TO_CATEGORY, PAGE_CATEGORY + pageCategory)));
        rows.add(List.of(createWebAppButton(chatId, botProperties.getUrlWeb())));
        rows.add(List.of(navButton(SEARCH_COURSE, "SEARCH")));
        markup.setKeyboard(rows);
        return markup;
    }

    private InlineKeyboardMarkup lessonsSearchButtons(Long courseId, int pageLessons, int pageCourses, String keyWord, Long chatId) {

        Page<Lesson> lessonPage = lessonService.findByPage(courseId, pageLessons);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        lessonPage.get().forEach(lesson -> {
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText(lesson.getTitle());
                    button.setCallbackData(LESSON_SEARCH + lesson.getId() +
                            DELIMITER_PAGE + courseId +
                            DELIMITER_PAGE + pageLessons +
                            DELIMITER_PAGE + pageCourses +
                            DELIMITER_PAGE + keyWord
                    );
                    rows.add(List.of(button));
                }
        );

        List<InlineKeyboardButton> navButtons = new ArrayList<>();

        if (pageLessons > 0) navButtons.add(navButton(PREVIOUS,
                PAGE_LESSON_SEARCH + courseId +
                        DELIMITER_PAGE + (pageLessons - 1) +
                        DELIMITER_PAGE + pageCourses +
                        DELIMITER_PAGE + keyWord
        ));

        if (lessonPage.hasNext()) navButtons.add(navButton(NEXT,
                PAGE_LESSON_SEARCH + courseId +
                        DELIMITER_PAGE + (pageLessons + 1) +
                        DELIMITER_PAGE + pageCourses +
                        DELIMITER_PAGE + keyWord
        ));

        if (!navButtons.isEmpty()) {
            rows.add(navButtons);
        }

        rows.add(List.of(navButton(BACK_TO_SEARCH_COURSE, PAGE_COURSE_SEARCH + pageCourses +
                DELIMITER_PAGE + keyWord)));
        rows.add(List.of(navButton(BACK_TO_CATEGORY, PAGE_CATEGORY + 0)));
        rows.add(List.of(createWebAppButton(chatId, botProperties.getUrlWeb())));
        rows.add(List.of(navButton(SEARCH_COURSE, "SEARCH")));
        markup.setKeyboard(rows);
        return markup;
    }
}
