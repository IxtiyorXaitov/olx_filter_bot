package dev.ikhtiyor.olxfilterbot.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author IkhtiyorDev
 * Date 17/02/22
 **/

public interface BotService {
    BotApiMethod<?> onWebhookUpdateReceived(Update update);
}
