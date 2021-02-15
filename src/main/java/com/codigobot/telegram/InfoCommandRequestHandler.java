/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020 Sergio del Amo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codigobot.telegram;

import com.microchatbots.core.parser.SpaceParser;
import com.microchatbots.core.parser.TextParser;
import com.microchatbots.core.parser.UserParser;
import com.microchatbots.telegrambots.api.TelegramBotConfiguration;
import com.microchatbots.telegrambots.core.Update;
import com.microchatbots.telegrambots.handler.SendMessageCommandHandler;

import javax.inject.Singleton;
import java.io.Serializable;
import java.util.Optional;

@Singleton
public class InfoCommandRequestHandler extends SendMessageCommandHandler {

    private final UserParser<Update> userParser;

    public InfoCommandRequestHandler(SpaceParser<Update> spaceParser,
                                     TextParser<Update> textParser,
                                     UserParser<Update> userParser) {
        super(spaceParser, textParser);
        this.userParser = userParser;
    }

    @Override
    protected String getCommandName() {
        return "info";
    }

    @Override
    protected String getText(TelegramBotConfiguration telegramBotConfiguration, Update update) {
        String text = "";
        Optional<Serializable> chatId = spaceParser.parseSpaceUniqueIdentifier(telegramBotConfiguration, update);
        if (chatId.isPresent()) {
            String id = chatId.get().toString();
            text += "chat id: " + id;
        }
        Optional<Serializable> userId = userParser.parseUserUniqueIdentifier(telegramBotConfiguration, update);

        if (userId.isPresent()) {
            String id = userId.get().toString();
            text += "user id: " + id;
        }
        return text;
    }
}
