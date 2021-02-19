package com.codigobot.telegram.rss

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.microchatbots.telegrambots.awslambda.Handler
import io.micronaut.http.HttpStatus
import spock.lang.AutoCleanup
import spock.lang.Shared

class InvalidTokenSpec extends ApplicationContextSpecification {

    void "invalid token returns 401"() {
        given:
        APIGatewayProxyRequestEvent request = createRequest(handler, '/help')
        request.path = '/bogus'

        when:
        APIGatewayProxyResponseEvent responseEvent = handler.execute(request)

        then:
        responseEvent.statusCode == HttpStatus.UNAUTHORIZED.code
    }
}
