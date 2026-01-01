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
import whiskey.code.courses.entity.Course;
import whiskey.code.courses.service.ButtonService;
import whiskey.code.courses.service.ButtonSearchService;
import whiskey.code.courses.service.db.CourseService;

import java.util.ArrayList;
import java.util.List;

import static whiskey.code.courses.util.Constants.*;
import static whiskey.code.courses.util.Utils.createWebAppButton;
import static whiskey.code.courses.util.Utils.navButton;


@Service
@RequiredArgsConstructor
public class CourseButtonServiceImpl implements ButtonService, ButtonSearchService {

    private final CourseService courseService;
    private final BotProperties botProperties;
    private final static String TEXT_COURSES = "\uD83C\uDFF0 Выберите курс:";
    private final static String SEARCH_COURSES = "🔎 Найденные курсы:";


    public SendMessage getButtons(Message message, CallbackQuery callbackQuery) {

        Long chatId = message.getChatId();

        String[] coursesInfo = callbackQuery.getData().split("_");
        long categoryId = Long.parseLong(coursesInfo[2]);
        int pageCourse = Integer.parseInt(coursesInfo[3]);
        int pageCategory = Integer.parseInt(coursesInfo[4]);

        return SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(TEXT_COURSES)
                .replyMarkup(coursesButtons(categoryId, pageCourse, pageCategory, chatId)).build();
    }

    public EditMessageText updateButtons(CallbackQuery callbackQuery) {

        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        String[] coursesInfo = callbackQuery.getData().split("_");
        long categoryId = Long.parseLong(coursesInfo[1]);
        int pageCourse = Integer.parseInt(coursesInfo[2]);
        int pageCategory = Integer.parseInt(coursesInfo[3]);

        return EditMessageText.builder()
                .chatId(String.valueOf(chatId))
                .messageId(messageId)
                .text(TEXT_COURSES)
                .replyMarkup(coursesButtons(categoryId, pageCourse, pageCategory, chatId))
                .build();

    }

    @Override
    public SendMessage getSearchButtons(Message message, CallbackQuery callbackQuery) {
        Long chatId = message.getChatId();
        String keyWord = message.getText();

        return SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(SEARCH_COURSES)
                .replyMarkup(coursesButtonsSearch(0, keyWord, chatId)).build();
    }

    @Override
    public EditMessageText updateSearchButtons(CallbackQuery callbackQuery) {

        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        String[] coursesSearchInfo = callbackQuery.getData().split("_");
        int pageCourse = Integer.parseInt(coursesSearchInfo[1]);

        return EditMessageText.builder()
                .chatId(String.valueOf(chatId))
                .messageId(messageId)
                .text(SEARCH_COURSES)
                .replyMarkup(coursesButtonsSearch(pageCourse, coursesSearchInfo[2], chatId))
                .build();
    }

    private InlineKeyboardMarkup coursesButtons(Long categoryId, int pageCourse, int pageCategory, Long chatId) {
        Page<Course> pageCourses = courseService.findByVisibilityTruePage(pageCourse, categoryId);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        pageCourses.get().forEach(course -> {
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText(course.getTitle());
                    button.setCallbackData(PAGE_LESSON + course.getId()
                            + ZERO_PAGE + DELIMITER_PAGE
                            + pageCourse + DELIMITER_PAGE
                            + pageCategory + DELIMITER_PAGE
                            + categoryId
                    );
                    rows.add(List.of(button));
                }
        );

        List<InlineKeyboardButton> navButtons = new ArrayList<>();


        if (pageCourse > 0) navButtons.add(navButton(PREVIOUS,
                PAGE_COURSE + categoryId +
                        DELIMITER_PAGE + (pageCourse - 1) +
                        DELIMITER_PAGE + pageCategory));

        if (pageCourses.hasNext()) navButtons.add(navButton(NEXT,
                PAGE_COURSE + categoryId +
                        DELIMITER_PAGE + (pageCourse + 1) +
                        DELIMITER_PAGE + pageCategory));

        if (!navButtons.isEmpty()) {
            rows.add(navButtons);
        }

        rows.add(List.of(navButton(BACK_TO_CATEGORY, PAGE_CATEGORY + pageCategory)));
        rows.add(List.of(createWebAppButton(chatId, botProperties.getUrlWeb())));
        rows.add(List.of(navButton(SEARCH_COURSE, "SEARCH")));

        markup.setKeyboard(rows);
        return markup;
    }

    private InlineKeyboardMarkup coursesButtonsSearch(int pageCourse, String keyWord, Long chatId) {
        Page<Course> pageCourses = courseService.searchCourses(keyWord, pageCourse);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        pageCourses.get().forEach(course -> {
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText(course.getTitle());
                    button.setCallbackData(PAGE_LESSON_SEARCH + course.getId()
                            + ZERO_PAGE + DELIMITER_PAGE
                            + pageCourse + DELIMITER_PAGE
                            + keyWord
                    );
                    rows.add(List.of(button));
                }
        );

        List<InlineKeyboardButton> navButtons = new ArrayList<>();


        if (pageCourse > 0) navButtons.add(navButton(PREVIOUS,
                PAGE_COURSE_SEARCH + (pageCourse - 1) +
                        DELIMITER_PAGE + keyWord));

        if (pageCourses.hasNext()) navButtons.add(navButton(NEXT,
                PAGE_COURSE_SEARCH + (pageCourse - 1) +
                        DELIMITER_PAGE + keyWord));

        if (!navButtons.isEmpty()) {
            rows.add(navButtons);
        }

        if (pageCourses.isEmpty()) {
            rows.add(List.of(navButton("\uD83D\uDD0D Ничего не найдено, повторить?", "SEARCH")));
        }

        rows.add(List.of(createWebAppButton(chatId, botProperties.getUrlWeb())));
        rows.add(List.of(navButton(BACK_TO_CATEGORY, PAGE_CATEGORY + 0)));

        if (!pageCourses.isEmpty()) {
            rows.add(List.of(navButton(SEARCH_COURSE, "SEARCH")));
        }

        markup.setKeyboard(rows);
        return markup;
    }
}
