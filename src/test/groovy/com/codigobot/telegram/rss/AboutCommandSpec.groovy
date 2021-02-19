package com.codigobot.telegram.rss

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.microchatbots.telegrambots.awslambda.Handler
import groovy.json.JsonSlurper
import io.micronaut.http.HttpStatus
import spock.lang.AutoCleanup
import spock.lang.Shared

class AboutCommandSpec extends ApplicationContextSpecification {

    void "/about renders about.md"() {
        APIGatewayProxyRequestEvent request = createRequest(handler, '/about')

        when:
        APIGatewayProxyResponseEvent responseEvent = handler.execute(request)

        then:
        responseEvent.statusCode == HttpStatus.OK.code

        when:
        def resp = new JsonSlurper().parseText(responseEvent.body)

        then:
        resp.method == 'sendMessage'
        resp.text == "[Codigo Bot](https://codigobot.com) es un podcast de [Kini](https://kinisoftware.com) y [Sergio](https://sergiodelamo.com).\n\nEste Bot de Telegram está desarrollado con 💙 por [Sergio del Amo](https://twitter.com/sdelamo) usando [Microchatbots](https://microchatbots.com) y [Micronaut](https://micronaut.io). "
        resp.chat_id == "654074321"
        resp.parse_mode == "Markdown"
    }
}
