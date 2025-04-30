package whiskey.code.courses.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import whiskey.code.courses.entity.Course;
import whiskey.code.courses.service.ButtonService;
import whiskey.code.courses.service.db.CourseService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ButtonServiceImpl implements ButtonService {

    private final CourseService courseService;
    private final static String TEXT = "📚 Выберите курс:";
    private final static String PREVIOUS = "◀ Назад";
    private final static String NEXT = "Далее ▶";
    private final static String PAGE = "page_";
    private final static String COURSE = "course_";

    public SendMessage courses(Long chatId, int page) {

        return SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(TEXT)
                .replyMarkup(coursesButtons(page)).build();
    }

    public EditMessageText updateCourses(Long chatId, int page, Integer messageId){

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

        if(page >0){
            InlineKeyboardButton prevButton = new InlineKeyboardButton();
            prevButton.setText(PREVIOUS);
            prevButton.setCallbackData(PAGE + (page - 1));
            navButtons.add(prevButton);
        }

        pageCourses.get().forEach(course -> {
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText(course.getTitle());
                    button.setCallbackData(COURSE + course.getId());
                    rows.add(List.of(button));
                }
        );

        if(pageCourses.hasNext()){
            InlineKeyboardButton nextButton = new InlineKeyboardButton();
            nextButton.setText(NEXT);
            nextButton.setCallbackData(PAGE + (page + 1));
            navButtons.add(nextButton);
        }

        if(!navButtons.isEmpty()){
            rows.add(navButtons);
        }

        markup.setKeyboard(rows);
        return markup;
    }

}
