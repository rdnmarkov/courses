package whiskey.code.courses.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import whiskey.code.courses.entity.Course;
import whiskey.code.courses.service.CourseButtonService;

import java.util.ArrayList;
import java.util.List;

import static whiskey.code.courses.util.Constants.PREVIOUS;
import static whiskey.code.courses.util.Constants.NEXT;
import static whiskey.code.courses.util.Constants.PAGE_LESSON;
import static whiskey.code.courses.util.Constants.PAGE_COURSE;
import static whiskey.code.courses.util.Constants.ZERO_PAGE;


@Service
@RequiredArgsConstructor
public class CourseButtonServiceImpl implements CourseButtonService {

    private final whiskey.code.courses.service.db.CourseService courseService;
    private final static String TEXT = "📚 Выберите курс:";


    public SendMessage courses(Long chatId, int page) {

        return SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(TEXT)
                .replyMarkup(coursesButtons(page)).build();
    }

    public EditMessageText updateCourses(Long chatId, int page, Integer messageId) {

        return EditMessageText.builder()
                .chatId(String.valueOf(chatId))
                .messageId(messageId)
                .text(TEXT)
                .replyMarkup(coursesButtons(page))
                .build();

    }

    private InlineKeyboardMarkup coursesButtons(int page) {

        Page<Course> pageCourses = courseService.findByVisibilityTruePage(page);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> navButtons = new ArrayList<>();

        if (page > 0) {
            InlineKeyboardButton prevButton = new InlineKeyboardButton();
            prevButton.setText(PREVIOUS);
            prevButton.setCallbackData(PAGE_COURSE + (page - 1));
            navButtons.add(prevButton);
        }

        pageCourses.get().forEach(course -> {
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText(course.getTitle());
                    button.setCallbackData(PAGE_LESSON + course.getId() + ZERO_PAGE);
                    rows.add(List.of(button));
                }
        );

        if (pageCourses.hasNext()) {
            InlineKeyboardButton nextButton = new InlineKeyboardButton();
            nextButton.setText(NEXT);
            nextButton.setCallbackData(PAGE_COURSE + (page + 1));
            navButtons.add(nextButton);
        }

        if (!navButtons.isEmpty()) {
            rows.add(navButtons);
        }

        markup.setKeyboard(rows);
        return markup;
    }

}
