package com.codigobot.telegram.rss

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.microchatbots.telegrambots.awslambda.Handler
import groovy.json.JsonSlurper
import io.micronaut.http.HttpStatus
import spock.lang.AutoCleanup
import spock.lang.Shared

class InfoCommandSpec extends ApplicationContextSpecification {

    void "/info outputs user id and chat id"() {
        APIGatewayProxyRequestEvent request = createRequest(handler, '/info')

        when:
        APIGatewayProxyResponseEvent responseEvent = handler.execute(request)

        then:
        responseEvent.statusCode == HttpStatus.OK.code

        when:
        def resp = new JsonSlurper().parseText(responseEvent.body)

        then:
        resp.method == 'sendMessage'
        resp.text == "chat id: 654074321\nuser id: 654074321"
        resp.chat_id == "654074321"
    }
}
