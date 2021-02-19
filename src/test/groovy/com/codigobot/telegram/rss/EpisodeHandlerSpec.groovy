package com.codigobot.telegram.rss

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.microchatbots.telegrambots.awslambda.Handler
import com.microchatbots.telegrambots.core.Message
import com.microchatbots.telegrambots.core.send.SendAudio
import com.microchatbots.telegrambots.core.send.SendMessage
import groovy.json.JsonSlurper
import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Requires
import io.micronaut.core.io.socket.SocketUtils
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

class EpisodeHandlerSpec extends Specification implements ApiGatewayUtils {

    @Shared
    int telegramApiPort = SocketUtils.findAvailableTcpPort()

    void "/info outputs user id and chat id"() {
        given:
        EmbeddedServer telegramApi = ApplicationContext.run(EmbeddedServer, [
                'spec.name': 'EpisodeHandlerSpecTelegramApi',
                'micronaut.server.port': telegramApiPort,
        ])
        Handler handler = new Handler(ApplicationContext.run([
                'spec.name': 'EpisodeHandlerSpec',
                'telegram.client.url': "http://localhost:$telegramApiPort",
        ]))
        APIGatewayProxyRequestEvent request = createRequest(handler, '/episode')

        when:
        APIGatewayProxyResponseEvent responseEvent = handler.execute(request)

        then:
        responseEvent.statusCode == HttpStatus.OK.code
        !responseEvent.body

        and:
        new PollingConditions().eventually {
            assert telegramApi.applicationContext.getBean(SendMessageController).messages
            assert telegramApi.applicationContext.getBean(SendAudioController).audios
        }

        cleanup:
        handler.close()
        telegramApi.close()
    }

    @Requires(property = 'spec.name', value = 'EpisodeHandlerSpecTelegramApi')
    @Controller
    static class SendAudioController {
        List<SendAudio> audios = []
        @Post("/botxxxyyyzzz/sendAudio")
        HttpResponse<Message> sendMessage(SendAudio sendAudio) {
            audios << sendAudio
            HttpResponse.ok(new Message())
        }
    }

    @Requires(property = 'spec.name', value = 'EpisodeHandlerSpecTelegramApi')
    @Controller
    static class SendMessageController {
        List<SendMessage> messages = []

        @Post("/botxxxyyyzzz/sendMessage")
        HttpResponse<Message> sendMessage(SendMessage sendMessage) {
            messages << sendMessage
            HttpResponse.ok(new Message())
        }
    }
}
