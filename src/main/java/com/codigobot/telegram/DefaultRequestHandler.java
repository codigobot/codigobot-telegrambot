package com.codigobot.telegram;

import com.microchatbots.core.parser.SpaceParser;
import com.microchatbots.core.parser.TextParser;
import com.microchatbots.telegrambots.api.TelegramBotConfiguration;
import com.microchatbots.telegrambots.core.Update;
import com.microchatbots.telegrambots.handler.SendMessageRequestHandler;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class DefaultRequestHandler extends SendMessageRequestHandler {
    public DefaultRequestHandler(SpaceParser<Update> spaceParser, TextParser<Update> textParser) {
        super(spaceParser, textParser);
    }

    @Override
    public Optional<String> getResponse(String text) {
        return Optional.of("I can hear you");
    }

    @Override
    public boolean canHandle(TelegramBotConfiguration telegramBotConfiguration, Update update) {
        return true;
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
