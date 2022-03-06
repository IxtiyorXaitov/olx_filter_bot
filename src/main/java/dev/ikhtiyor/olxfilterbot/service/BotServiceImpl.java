package dev.ikhtiyor.olxfilterbot.service;

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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collections;

import static dev.ikhtiyor.olxfilterbot.entity.enums.UserStepEnum.*;

/**
 * @author IkhtiyorDev
 * Date 17/02/22
 **/
@Service
@RequiredArgsConstructor
public class BotServiceImpl extends TelegramWebhookBot implements BotService {


    private final OlxService olxService;
    private final ConnectionService connectionService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final BotService botService;

    private String startButton = "/start";

    @Value("${bot.username}")
    String username;

    @Value("${bot.token}")
    String token;

    @Value("${bot.webHookPath}")
    String webHookPath;

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

    private void sendMessage(long chatId, String textMessage) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(textMessage);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void shareYourContact(Long chatId) {
        SendMessage sendMessage = new SendMessage();

        ReplyKeyboardMarkup replyKeyboardMarkup = makeReplyMarkup();
        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton("Share your contact");
        keyboardButton.setRequestContact(true);
        keyboardRow.add(keyboardButton);
        replyKeyboardMarkup.setKeyboard(Collections.singletonList(keyboardRow));

        sendMessage.setText("Share your contact");
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public ReplyKeyboardMarkup makeReplyMarkup() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        return replyKeyboardMarkup;
    }

    private void setStartStepUser(String message, User user) {
        if (startButton.equals(message)) {

            user.setUserStep(START);
            try {
                execute(new SendMessage(user.getChatId(), "Hello"));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            userRepository.save(user);

        }
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
