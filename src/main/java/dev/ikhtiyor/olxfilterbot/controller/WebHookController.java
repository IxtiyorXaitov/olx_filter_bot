package dev.ikhtiyor.olxfilterbot.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author IkhtiyorDev  <br/>
 * Date 17/02/22
 **/

@RequestMapping("/")
public interface WebHookController {

    @PostMapping
    BotApiMethod<?> onUpdateReceived(@RequestBody Update update);
}
