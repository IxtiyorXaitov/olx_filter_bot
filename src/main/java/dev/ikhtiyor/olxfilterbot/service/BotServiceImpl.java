package dev.ikhtiyor.olxfilterbot.service;

import dev.ikhtiyor.olxfilterbot.entity.User;
import dev.ikhtiyor.olxfilterbot.entity.enums.UserStepEnum;
import dev.ikhtiyor.olxfilterbot.exceptions.TelegramBotException;
import dev.ikhtiyor.olxfilterbot.payload.ListItemDTO;
import dev.ikhtiyor.olxfilterbot.payload.MainCategoryDTO;
import dev.ikhtiyor.olxfilterbot.repository.UserRepository;
import dev.ikhtiyor.olxfilterbot.utils.MessageConstraints;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author IkhtiyorDev
 * Date 17/02/22
 **/
@Service
@RequiredArgsConstructor
public class BotServiceImpl implements BotService {

    private final UserService userService;
    private final OlxService olxService;
    private final UserRepository userRepository;
    private final ConnectionService connectionService;

    @Override
    public User checkUserIfNotExistCreate(Update update) {

        Long chatId = null;

        if (update.hasMessage()) {
            Message message = update.getMessage();
            chatId = message.getChatId();
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            chatId = callbackQuery.getMessage().getChatId();
        }

        return userService.checkUserIfNotExistCreate(chatId);
    }

    @Override
    public void checkUserEnterStartCommand(Update update, User user) {
        if (update.hasMessage()) {
            String text = update.getMessage().getText();

            if (text.equals("/start")) {
                start(update, user);
            }
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

        checkHasContact(update);

        Contact contact = update.getMessage().getContact();

        user.setPhoneNumber(contact.getPhoneNumber());
        setUserStep(user, UserStepEnum.MAIN_CATEGORY);

        return sendMainCategory(user.getChatId(), MessageConstraints.PLEASE_SELECT_CATEGORY);

    }

    @Override
    public SendMessage mainCategory(Update update, User user) {

        checkHasCallbackQuery(update);

        CallbackQuery callbackQuery = update.getCallbackQuery();

        String menuUrl = callbackQuery.getData();

        List<ListItemDTO> allCategoryItems = getAllCategoryItems(menuUrl);

        for (ListItemDTO allCategoryItem : allCategoryItems) {
            System.out.println(allCategoryItem);
        }

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
        SendMessage sendMessage = createSendMessage(chatId);
        sendMessage.setText(message);
        return sendMessage;
    }

    private void setUserStep(User user, UserStepEnum userStepEnum) {

        user.setUserStep(userStepEnum);
        userRepository.save(user);

    }

    private SendMessage sendReplyKeyboardMarkupMessage(Long chatId, String message, String buttonText) {

        SendMessage sendMessage = createSendMessage(chatId);
        ReplyKeyboardMarkup replyKeyboardMarkup = makeReplyMarkup();
        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton(buttonText);
        keyboardButton.setRequestContact(true);
        keyboardRow.add(keyboardButton);
        replyKeyboardMarkup.setKeyboard(Collections.singletonList(keyboardRow));

        sendMessage.setText(message);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        return sendMessage;
    }

    private InlineKeyboardMarkup makeInline(List<List<InlineKeyboardButton>> keyboard) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(keyboard);
        return inlineKeyboardMarkup;
    }

    private ReplyKeyboardMarkup makeReplyMarkup() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        return replyKeyboardMarkup;
    }

    private SendMessage sendMainCategory(Long chatId, String message) {
        SendMessage sendMessage = createSendMessage(chatId);

        List<InlineKeyboardButton> keyboardButtonsForMainCategories = getInlineKeyboardButtonsForMainCategories();

        List<List<InlineKeyboardButton>> result = makeInlineKeyboardButtonList(keyboardButtonsForMainCategories);

        InlineKeyboardMarkup inlineKeyboardMarkup = makeInline(result);

        sendMessage.setText(message);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        return sendMessage;
    }

    private List<List<InlineKeyboardButton>> makeInlineKeyboardButtonList(List<InlineKeyboardButton> keyboardButtonsForMainCategories) {

        List<List<InlineKeyboardButton>> result = new ArrayList<>();
        int size = keyboardButtonsForMainCategories.size();

        for (int i = 0; i < size; i++) {

            List<InlineKeyboardButton> list = new ArrayList<>();

            if (i + 1 == keyboardButtonsForMainCategories.size()) {
                break;
            }

            list.add(keyboardButtonsForMainCategories.get(i));
            list.add(keyboardButtonsForMainCategories.get(i + 1));

            result.add(list);
        }

        return result;
    }

    private List<InlineKeyboardButton> getInlineKeyboardButtonsForMainCategories() {

        return getAllMainCategories()
                .stream()
                .map(mainCategoryDTO -> {
                    return makeInlineKeyboardButtonTextAndCallBackData(mainCategoryDTO.getTitle(), mainCategoryDTO.getUrl());
                })
                .collect(Collectors.toList());

    }

    private SendMessage createSendMessage(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        return sendMessage;
    }

    private List<MainCategoryDTO> getAllMainCategories() {

        Document doc = connectionService.connectToPage("https://www.olx.uz/");

        return olxService.getAllMainCategories(doc);
    }

    private List<ListItemDTO> getAllCategoryItems(String url) {

        Document doc = connectionService.connectToPage(url);

        return olxService.getAllListItems(doc);
    }

    private InlineKeyboardButton makeInlineKeyboardButtonTextAndCallBackData(String text, String callBackDate) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();

        inlineKeyboardButton.setText(text);
        inlineKeyboardButton.setCallbackData(callBackDate);

        return inlineKeyboardButton;
    }

    private void checkHasMessage(Update update) {
        if (!update.hasMessage()) {
            throw TelegramBotException.botThrow(update.getMessage().getChatId(), "Wrong input data!");
        }
    }

    private void checkHasCallbackQuery(Update update) {
        if (!update.hasCallbackQuery()) {
            throw TelegramBotException.botThrow(update.getMessage().getChatId(), "Wrong input data!");
        }
    }

    private void checkHasContact(Update update) {

        checkHasMessage(update);

        if (!update.getMessage().hasContact()) {
            throw TelegramBotException.botThrow(update.getMessage().getChatId(), "Wrong input data!");
        }
    }

}
