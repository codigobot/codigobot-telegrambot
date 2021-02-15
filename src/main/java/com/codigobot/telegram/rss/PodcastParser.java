package com.codigobot.telegram.rss;

import io.micronaut.context.exceptions.ConfigurationException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class PodcastParser {
    private static final String ITEM = "item";
    private static final String TITLE = "title";
    private static final String ENCLOSURE = "enclosure";
    private static final String CONTENT_ENCODED = "encoded";
    private static final String EPISODE = "episode";
    private static final String URL = "url";
    public static final EpisodeComparator EPISODE_COMPARATOR = new EpisodeComparator();
    private final URL url;

    public PodcastParser(String feedUrl) {
        try {
            this.url = new URL(feedUrl);
        } catch (MalformedURLException e) {
            throw new ConfigurationException("feedUrl " + feedUrl + " throws MalformedUrlException");
        }
    }

    public Podcast readPodcast() {
        Podcast podcast = null;
        try {
            boolean isFeedHeader = true;
            String showNotes = "";
            String title = "";
            String mp3 = "";
            Integer number = null;

            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            try (InputStream in = url.openStream()) {
                XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
                while (eventReader.hasNext()) {
                    XMLEvent event = eventReader.nextEvent();
                    if (event.isStartElement()) {
                        String localPart = event.asStartElement().getName()
                                .getLocalPart();
                        switch (localPart) {
                            case ITEM:
                                if (isFeedHeader) {
                                    isFeedHeader = false;
                                    podcast = new Podcast();
                                }
                                event = eventReader.nextEvent();
                                break;
                            case TITLE:
                                title = getCharacterData(event, eventReader);
                                break;
                            case EPISODE:
                                number = Integer.valueOf(getCharacterData(event, eventReader));
                                break;
                            case ENCLOSURE:
                                mp3 = event.asStartElement().getAttributeByName(QName.valueOf(URL)).getValue();
                                break;
                            case CONTENT_ENCODED:
                                XMLEvent e = eventReader.nextEvent();
                                if (e instanceof Characters) {
                                   do {
                                       showNotes += e.asCharacters().getData();
                                       e = eventReader.nextEvent();
                                   } while (e instanceof Characters);
                                }
                                break;
                            default:
                                break;
                        }
                    } else if (event.isEndElement()) {
                        if (event.asEndElement().getName().getLocalPart().equalsIgnoreCase(ITEM)) {
                            Episode episode = new Episode();

                            episode.setShowNotes(showNotes);
                            episode.setTitle(title);
                            episode.setUrl(mp3);
                            episode.setNumber(number);
                            podcast.getEpisodes().add(episode);
                            event = eventReader.nextEvent();
                            continue;
                        }
                    }
                }
            }
        } catch (IOException | XMLStreamException e) {
            throw new RuntimeException(e);
        }
        podcast.getEpisodes().sort(EPISODE_COMPARATOR);
        return podcast;
    }

    private String getCharacterData(XMLEvent event, XMLEventReader eventReader)
            throws XMLStreamException {
        String result = "";
        event = eventReader.nextEvent();
        if (event instanceof Characters) {
            result = event.asCharacters().getData();
        }
        return result;
    }
}
