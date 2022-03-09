package dev.ikhtiyor.olxfilterbot.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TelegramBotException extends RuntimeException {

    private Long chatId;
    private String message;

    public static TelegramBotException botThrow(Long chatId, String message) {
        return new TelegramBotException(chatId, message);
    }
}
