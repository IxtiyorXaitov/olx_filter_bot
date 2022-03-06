package dev.ikhtiyor.olxfilterbot.bot;

import dev.ikhtiyor.olxfilterbot.entity.User;
import dev.ikhtiyor.olxfilterbot.entity.enums.UserStepEnum;
import dev.ikhtiyor.olxfilterbot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static dev.ikhtiyor.olxfilterbot.entity.enums.UserStepEnum.*;

/**
 * @author IkhtiyorDev  <br/>
 * Date 06/03/22
 **/

@Service
@RequiredArgsConstructor
public class TelegramBot extends TelegramWebhookBot {

    private final UserRepository userRepository;

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {

        if (update.hasMessage()) {

            Message message = update.getMessage();

            if (message.hasText()) {

                String text = message.getText();

                if (text.equals("/start")) {

                    User user = userService.checkAndCreateUser();

                } else {

                }

            }

        }

        Message message = update.getMessage();


        UserStepEnum userStep = user.getUserStep();

        switch (userStep) {
            case START:

                sendMessage(chatId, "Hello bro. Enter your first name 😎");


                user.setUserStep(WELCOMING_FIRST_NAME);
                userRepository.save(user);
                break;
            case WELCOMING_FIRST_NAME:

                user.setFirstName(message.getText());
                sendMessage(chatId, "Hello bro. Enter your last name 😎");


                user.setUserStep(WELCOMING_LAST_NAME);
                userRepository.save(user);
                break;
            case WELCOMING_LAST_NAME:

                user.setLastName(message.getText());

                shareYourContact(chatId);

                user.setUserStep(WELCOMING_PHONE_NUMBER);
                userRepository.save(user);
                break;
            case WELCOMING_PHONE_NUMBER:

                user.setPhoneNumber(message.getText());

                user.setUserStep(MAIN_CATEGORY);
                userRepository.save(user);
                break;
            case MAIN_CATEGORY:
                user.setUserStep(SUB_CATEGORY);


                userRepository.save(user);
                break;
            case SUB_CATEGORY:
                user.setUserStep(ITEM_LIST);


                userRepository.save(user);
                break;
            case ITEM_LIST:
                user.setUserStep(ITEM);


                userRepository.save(user);
                break;
            case ITEM:
                user.setUserStep(null);


                userRepository.save(user);
                break;

            default:
                sendMessage(user.getChatId(), "Error 🤯");

        }


        return new SendMessage();

    }

    @Value("${bot.username}")
    String username;

    @Value("${bot.token}")
    String token;

    @Value("${bot.webHookPath}")
    String webHookPath;


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
