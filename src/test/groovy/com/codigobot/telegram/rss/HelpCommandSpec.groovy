package com.codigobot.telegram.rss

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.microchatbots.telegrambots.awslambda.Handler
import groovy.json.JsonSlurper
import io.micronaut.http.HttpStatus
import spock.lang.AutoCleanup
import spock.lang.Shared

class HelpCommandSpec extends ApplicationContextSpecification {

    void "/about renders about.md"() {
        APIGatewayProxyRequestEvent request = createRequest(handler, '/help')

        when:
        APIGatewayProxyResponseEvent responseEvent = handler.execute(request)

        then:
        responseEvent.statusCode == HttpStatus.OK.code

        when:
        def resp = new JsonSlurper().parseText(responseEvent.body)

        then:
        resp.method == 'sendMessage'
        resp.text == "Este bot expone los siguientes comandos: \n" +
                "\n" +
                "/help - como usar este bot\n" +
                "\n" +
                "/about - información sobre este bot\n" +
                "\n" +
                "/info - devuelve el identificador de chat y usuario\n" +
                "\n" +
                "/episode - escucha el último episodio y las notas del mismo"
        resp.chat_id == "654074321"
        resp.parse_mode == "Markdown"
    }
}
