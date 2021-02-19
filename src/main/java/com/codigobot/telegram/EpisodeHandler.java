package com.codigobot.telegram;

import com.codigobot.telegram.rss.Episode;
import com.codigobot.telegram.rss.Podcast;
import com.codigobot.telegram.rss.PodcastParser;
import com.microchatbots.core.parser.SpaceParser;
import com.microchatbots.core.parser.TextParser;
import com.microchatbots.core.request.handler.CommandHandler;
import com.microchatbots.telegrambots.api.BlockingTelegramBot;
import com.microchatbots.telegrambots.api.TelegramBotConfiguration;
import com.microchatbots.telegrambots.core.Message;
import com.microchatbots.telegrambots.core.Update;
import com.microchatbots.telegrambots.core.send.SendAudio;
import com.microchatbots.telegrambots.core.send.SendMessage;
import com.microchatbots.telegrambots.handler.TelegramRequestHandler;
import edu.umd.cs.findbugs.annotations.NonNull;
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
        if (podcast.getEpisodes().isEmpty()) {
            return null;
        }
        Episode episode = podcast.getEpisodes().get(0);
        Optional<Serializable> chatIdOptional = spaceParser.parseSpaceUniqueIdentifier(telegramBotConfiguration, update);
        chatIdOptional.ifPresent(serializable -> {
            String chatId = serializable.toString();
            sendAudio(chatId, episode);
            sendMessage(chatId, episode);
        });
        return null;
    }

    private void sendAudio(@NonNull String chatId, @NonNull Episode episode) {
        SendAudio sendAudio = composeAudio(chatId, episode);
        try {
            Message rsp = telegramBot.sendAudio(sendAudio);
            if (LOG.isInfoEnabled()) {
                LOG.info("audio {} sent with response {}", sendAudio.toString(), rsp.toString());
            }
        } catch (Throwable t) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("error received sending audio {} error {}", sendAudio.toString(), t.getMessage());
            }
        }
    }

    private void sendMessage(@NonNull String chatId, @NonNull Episode episode) {

        SendMessage message = composeMessage(chatId, episode);
        try {
            Message rsp = telegramBot.sendMessage(message);
            if (LOG.isInfoEnabled()) {
                LOG.info("audio {} sent with response {}", message.toString(), rsp.toString());
            }
        } catch (Throwable t) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("error received sending message {} error {}", message.toString(), t.getMessage());
            }
        }
    }

    private SendMessage composeMessage(@NonNull String chatId, @NonNull Episode episode) {
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
