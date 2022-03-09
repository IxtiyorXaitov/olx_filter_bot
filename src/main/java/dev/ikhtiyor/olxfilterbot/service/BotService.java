package dev.ikhtiyor.olxfilterbot.service;

import dev.ikhtiyor.olxfilterbot.entity.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author IkhtiyorDev
 * Date 17/02/22
 **/

public interface BotService {

    User checkUserIfNotExistCreate(Update update);

    void checkUserEnterStartCommand(Update update, User user);

    SendMessage start(Update update, User user);

    SendMessage welcomingFirstName(Update update, User user);

    SendMessage welcomingLastName(Update update, User user);

    SendMessage welcomingPhoneNumber(Update update, User user);

    SendMessage mainCategory(Update update, User user);

    SendMessage subCategory(Update update, User user);

    SendMessage itemList(Update update, User user);

    SendMessage item(Update update, User user);

}
