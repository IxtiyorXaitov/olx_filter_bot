package dev.ikhtiyor.olxfilterbot.controller;

import dev.ikhtiyor.olxfilterbot.service.BotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;


@RestController
@RequiredArgsConstructor
public class WebHookControllerImpl implements WebHookController {
    private final BotService botService;

    @Override
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return botService.onWebhookUpdateReceived(update);
    }


}
