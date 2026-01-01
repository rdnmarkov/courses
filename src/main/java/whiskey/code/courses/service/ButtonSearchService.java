package whiskey.code.courses.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface ButtonSearchService {

    SendMessage getSearchButtons(Message message, CallbackQuery callbackQuery);

    EditMessageText updateSearchButtons(CallbackQuery callbackQuery);

}
