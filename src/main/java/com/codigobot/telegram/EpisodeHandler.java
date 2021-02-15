package com.codigobot.telegram;

import com.codigobot.telegram.rss.Episode;
import com.codigobot.telegram.rss.Podcast;
import com.codigobot.telegram.rss.PodcastParser;
import com.microchatbots.core.parser.SpaceParser;
import com.microchatbots.core.parser.TextParser;
import com.microchatbots.core.request.handler.CommandHandler;
import com.microchatbots.telegrambots.api.BlockingTelegramBot;
import com.microchatbots.telegrambots.api.DefaultTelegramBot;
import com.microchatbots.telegrambots.api.TelegramBot;
import com.microchatbots.telegrambots.api.TelegramBotConfiguration;
import com.microchatbots.telegrambots.core.Message;
import com.microchatbots.telegrambots.core.Update;
import com.microchatbots.telegrambots.core.send.SendAudio;
import com.microchatbots.telegrambots.core.send.SendMessage;
import com.microchatbots.telegrambots.handler.TelegramRequestHandler;
import io.micronaut.context.exceptions.ConfigurationException;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.inject.Singleton;
import java.io.Serializable;
import java.util.Optional;

@Singleton
public class EpisodeHandler extends CommandHandler<TelegramBotConfiguration, Update, Void> implements TelegramRequestHandler<Void> {
    private static final Logger LOG = LoggerFactory.getLogger(EpisodeHandler.class);

    public static final String COMMAND_EPISODE = "episode";

    private final Podcast podcast;
    private final BlockingTelegramBot telegramBot;

    public EpisodeHandler(SpaceParser<Update> spaceParser,
                          TextParser<Update> textParser,
                          WebsiteConfiguration websiteConfiguration,
                          @Named("codigobot") BlockingTelegramBot telegramBot) {
        super(spaceParser, textParser);
        this.podcast = new PodcastParser(websiteConfiguration.getRssUrl()).readPodcast();
        this.telegramBot = telegramBot;
    }

    @Override
    protected String getCommandName() {
        return COMMAND_EPISODE;
    }

    @Override
    public Void handle(TelegramBotConfiguration telegramBotConfiguration, Update update) {
        if (!podcast.getEpisodes().isEmpty()) {
            Episode episode = podcast.getEpisodes().get(0);
            Optional<Serializable> chatIdOptional = spaceParser.parseSpaceUniqueIdentifier(telegramBotConfiguration, update);
            chatIdOptional.ifPresent(serializable -> {
                String chatId = serializable.toString();
                try {
                    SendAudio sendAudio = composeAudio(chatId, episode);
                    Message rsp = telegramBot.sendAudio(sendAudio);
                    LOG.info("audio {} sent with response {}", sendAudio.toString(), rsp.toString());
                    SendMessage message = composeMessage(chatId, episode);
                    rsp = telegramBot.sendMessage(message);
                    LOG.info("message {} sent with response {}", sendAudio.toString(), rsp.toString());
                } catch (HttpClientResponseException e) {
                    LOG.warn("could not send message");
                }
            });
        }
        return null;
    }

    private SendMessage composeMessage(String chatId, Episode episode) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(cleanupHtml(episode.getShowNotes()));
        message.setParseMode("HTML");
        return message;
    }

    private String cleanupHtml(String showNotes) {
        String html = showNotes;
        html = html.replace("<p>", "");
        html = html.replace("</p>", "\n");
        html = html.replace("<ul>", "");
        html = html.replace("<li>", "- ");
        html = html.replace("</li>", "\n");
        html = html.replace("</ul>", "\n");
        return html;
    }

    private SendAudio composeAudio(String chatId, Episode episode) {
        SendAudio sendAudio = new SendAudio();
        sendAudio.setChatId(chatId);
        sendAudio.setAudio(episode.getUrl());
        sendAudio.setTitle(episode.getTitle());
        return sendAudio;
    }
}
