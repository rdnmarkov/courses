package whiskey.code.courses.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import whiskey.code.courses.entity.Course;
import whiskey.code.courses.service.ButtonService;
import whiskey.code.courses.service.db.CourseService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ButtonServiceImpl implements ButtonService {

    private final CourseService courseService;

    public SendMessage buttonsCourses(Long chatId){

        List<Course> courses = courseService.findByVisibilityTrue();

        SendMessage message = new SendMessage(String.valueOf(chatId), "📚 Выберите курс:");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (Course course : courses) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(course.getTitle());
            button.setCallbackData("course_" + course.getId());
            rows.add(List.of(button));
        }

        markup.setKeyboard(rows);
        message.setReplyMarkup(markup);

        return message;
    }

}
