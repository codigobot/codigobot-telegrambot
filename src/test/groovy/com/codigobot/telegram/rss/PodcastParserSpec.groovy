package com.codigobot.telegram.rss

import spock.lang.Specification

class PodcastParserSpec extends Specification {

    void "parse podcast feed"() {
        given:
        String url = 'https://feed.codigobot.com/rss.xml'
        PodcastParser parser = new PodcastParser(url)

        when:
        Podcast podcast = parser.readPodcast()

        then:
        podcast
    }
}
