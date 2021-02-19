package com.codigobot.telegram.rss

import com.microchatbots.telegrambots.awslambda.Handler
import spock.lang.AutoCleanup
import spock.lang.Shared
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
