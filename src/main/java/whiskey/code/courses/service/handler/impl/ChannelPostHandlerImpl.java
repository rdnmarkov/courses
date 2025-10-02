package whiskey.code.courses.service.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import whiskey.code.courses.bot.CoursesBot;
import whiskey.code.courses.config.properties.BotProperties;
import whiskey.code.courses.service.AdminPanelService;
import whiskey.code.courses.service.handler.UpdateHandler;
import whiskey.code.courses.util.Utils;

@Service("channelPostHandler")
@RequiredArgsConstructor
public class ChannelPostHandlerImpl implements UpdateHandler {

    private final BotProperties botProperties;
    private final AdminPanelService adminPanelService;

    @Override
    public boolean canHandle(Update update) {
        return update.hasChannelPost();
    }

    @Override
    public void handle(Update update, CoursesBot bot) {
        //В бот пришло сообщение из канала хранения уроков
        var chatId = update.getChannelPost().getChatId();
        Message channelPost = update.getChannelPost();

        if (chatId.equals(botProperties.getAdminChannel())) {
            Utils.sendMessage(adminPanelService.handleAdminCommand(channelPost), bot);
        }
    }
}
