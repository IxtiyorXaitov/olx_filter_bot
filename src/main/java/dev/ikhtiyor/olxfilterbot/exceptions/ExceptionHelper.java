package dev.ikhtiyor.olxfilterbot.exceptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ExceptionHelper {

    @ExceptionHandler(value = {TelegramBotException.class})
    public SendMessage handleException(TelegramBotException ex) {

        ex.printStackTrace();

        return new SendMessage(ex.getChatId().toString(), ex.getMessage());
    }

}

