package dev.ikhtiyor.olxfilterbot.controller;

import dev.ikhtiyor.olxfilterbot.bot.TelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;


@RestController
@RequiredArgsConstructor
public class WebHookControllerImpl implements WebHookController {

    private final TelegramBot telegramBot;

    @Override
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return telegramBot.onWebhookUpdateReceived(update);
    }


}
