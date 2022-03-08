package dev.ikhtiyor.olxfilterbot.service;

import dev.ikhtiyor.olxfilterbot.entity.User;
import dev.ikhtiyor.olxfilterbot.entity.enums.UserStepEnum;
import dev.ikhtiyor.olxfilterbot.repository.UserRepository;
import dev.ikhtiyor.olxfilterbot.utils.MessageConstraints;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Collections;

/**
 * @author IkhtiyorDev
 * Date 17/02/22
 **/
@Service
@RequiredArgsConstructor
public class BotServiceImpl implements BotService {

    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public User checkUserIfNotExistCreate(Update update) {

        if (update.hasMessage()) {
            Message message = update.getMessage();
            return userService.checkUserIfNotExistCreate(message.getChatId());
        } else {
            return null;
        }

    }

    @Override
    public SendMessage start(Update update, User user) {


        setUserStep(user, UserStepEnum.WELCOMING_FIRST_NAME);

        return sendTextMessage(user.getChatId(), MessageConstraints.PLEASE_ENTER_YOUR_FIRST_NAME);
    }

    @Override
    public SendMessage welcomingFirstName(Update update, User user) {

        String firstName = getUserEnteredTextMessage(update);
        user.setFirstName(firstName);
        setUserStep(user, UserStepEnum.WELCOMING_LAST_NAME);

        return sendTextMessage(user.getChatId(), MessageConstraints.PLEASE_ENTER_YOUR_LAST_NAME);
    }

    @Override
    public SendMessage welcomingLastName(Update update, User user) {

        String lastName = getUserEnteredTextMessage(update);
        user.setLastName(lastName);
        setUserStep(user, UserStepEnum.WELCOMING_PHONE_NUMBER);

        return sendReplyKeyboardMarkupMessage(
                user.getChatId(),
                MessageConstraints.PLEASE_ENTER_YOUR_PHONE_NUMBER,
                MessageConstraints.SHARE_CONTACT
        );
    }

    @Override
    public SendMessage welcomingPhoneNumber(Update update, User user) {

        Message message = update.getMessage();

        if (message.hasContact()) {

            Contact contact = message.getContact();

            user.setPhoneNumber(contact.getPhoneNumber());
            setUserStep(user, UserStepEnum.MAIN_CATEGORY);

            return sendTextMessage(user.getChatId(), MessageConstraints.PLEASE_SELECT_CATEGORY);

        }

        System.out.println(update);
        System.out.println(user);

        return null;
    }

    @Override
    public SendMessage mainCategory(Update update, User user) {
        return null;
    }

    @Override
    public SendMessage subCategory(Update update, User user) {
        return null;
    }

    @Override
    public SendMessage itemList(Update update, User user) {
        return null;
    }

    @Override
    public SendMessage item(Update update, User user) {
        return null;
    }

    private String getUserEnteredTextMessage(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                return message.getText();
            } else {
                // todo exception
                return null;
            }
        } else {
            // todo exception
            return null;
        }
    }

    private SendMessage sendTextMessage(Long chatId, String message) {
        return new SendMessage(
                chatId,
                message
        );
    }

    private void setUserStep(User user, UserStepEnum userStepEnum) {

        user.setUserStep(userStepEnum);
        userRepository.save(user);

    }

    private SendMessage sendReplyKeyboardMarkupMessage(Long chatId, String message, String buttonText) {

        SendMessage sendMessage = new SendMessage();
        ReplyKeyboardMarkup replyKeyboardMarkup = makeReplyMarkup();
        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton(buttonText);
        keyboardButton.setRequestContact(true);
        keyboardRow.add(keyboardButton);
        replyKeyboardMarkup.setKeyboard(Collections.singletonList(keyboardRow));

        sendMessage.setText(message);
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        return sendMessage;
    }

    private ReplyKeyboardMarkup makeReplyMarkup() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        return replyKeyboardMarkup;
    }
}
