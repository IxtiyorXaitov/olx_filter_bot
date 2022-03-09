package dev.ikhtiyor.olxfilterbot.bot;

import dev.ikhtiyor.olxfilterbot.entity.User;
import dev.ikhtiyor.olxfilterbot.repository.UserRepository;
import dev.ikhtiyor.olxfilterbot.service.BotService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

/**
 * @author IkhtiyorDev  <br/>
 * Date 06/03/22
 **/

@Service
@RequiredArgsConstructor
public class TelegramBot extends TelegramWebhookBot {

    private final UserRepository userRepository;
    private final BotService botService;


    @Value("${bot.username}")
    String username;

    @Value("${bot.token}")
    String token;

    @Value("${bot.webHookPath}")
    String webHookPath;

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {

            


            User user = botService.checkUserIfNotExistCreate(update);

            switch (user.getUserStep()) {
                case START:
                    execute(botService.start(update, user));
                    break;
                case WELCOMING_FIRST_NAME:
                    execute(botService.welcomingFirstName(update, user));
                    break;
                case WELCOMING_LAST_NAME:
                    execute(botService.welcomingLastName(update, user));
                    break;
                case WELCOMING_PHONE_NUMBER:
                    execute(botService.welcomingPhoneNumber(update, user));
                    break;
                case MAIN_CATEGORY:
                    execute(botService.mainCategory(update, user));
                    break;
                case SUB_CATEGORY:
                    execute(botService.subCategory(update, user));
                    break;
                case ITEM_LIST:
                    execute(botService.itemList(update, user));
                    break;
                case ITEM:
                    execute(botService.item(update, user));
                    break;
                default:
                    //
            }

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }
}
