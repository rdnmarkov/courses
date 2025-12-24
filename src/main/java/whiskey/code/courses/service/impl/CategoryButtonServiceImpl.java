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
import whiskey.code.courses.entity.Category;
import whiskey.code.courses.entity.Course;
import whiskey.code.courses.service.ButtonService;
import whiskey.code.courses.service.db.CategoryService;

import java.util.ArrayList;
import java.util.List;

import static whiskey.code.courses.util.Constants.*;
import static whiskey.code.courses.util.Utils.createWebAppButton;
import static whiskey.code.courses.util.Utils.navButton;


@Service
@RequiredArgsConstructor
public class CategoryButtonServiceImpl implements ButtonService {

    private final CategoryService categoryService;
    private final BotProperties botProperties;
    private final static String TEXT = "\uD83C\uDFF0 Выберите категорию:";


    public SendMessage getButtons(Message message, CallbackQuery callbackQuery) {

        Long chatId = message.getChatId();

        return SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(TEXT)
                .replyMarkup(categoryButtons(0, chatId)).build();
    }

    public EditMessageText updateButtons(CallbackQuery callbackQuery) {

        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        int page = Integer.parseInt(callbackQuery.getData().split("_")[1]);

        return EditMessageText.builder()
                .chatId(String.valueOf(chatId))
                .messageId(messageId)
                .text(TEXT)
                .replyMarkup(categoryButtons(page, chatId))
                .build();

    }

    private InlineKeyboardMarkup categoryButtons(int page, Long chatId) {

        Page<Category> pageCategory = categoryService.findByVisibilityTruePage(page);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        pageCategory.get().forEach(category -> {
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText(category.getTitle());
                    button.setCallbackData(PAGE_LESSON + category.getId()
                            + ZERO_PAGE + DELIMITER_PAGE + page);
                    rows.add(List.of(button));
                }
        );

        List<InlineKeyboardButton> navButtons = new ArrayList<>();

        if (page > 0) navButtons.add(navButton(PREVIOUS, PAGE_COURSE + (page - 1)));

        if (pageCategory.hasNext()) navButtons.add(navButton(NEXT, PAGE_COURSE + (page + 1)));

        if (!navButtons.isEmpty()) {
            rows.add(navButtons);
        }

        rows.add(List.of(createWebAppButton(chatId, botProperties.getUrlWeb())));

        markup.setKeyboard(rows);
        return markup;
    }


}
