package com.codigobot.telegram.rss

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.fasterxml.jackson.databind.ObjectMapper
import com.microchatbots.telegrambots.awslambda.Handler

trait ApiGatewayUtils {

    APIGatewayProxyRequestEvent createRequest(Handler handler, String text,
                                              int userId = 654074321,
                                              int chatId = 654074321,
                                              int updateId = 126317587,
                                              int messageId = 134) {
        createRequest(handler, [
                "update_id": updateId,
                "message":[
                        "message_id": messageId,
                        "from": [
                                "id": userId,
                                "is_bot": false,
                                "first_name": "Sergio",
                                "last_name": "Del Amo",
                                "language_code": "en"
                        ],
                        "chat": [
                                "id": chatId,
                                "first_name": "Sergio",
                                "last_name": "Del Amo",
                                "type": "private"
                        ],
                        "date": 1613542746,
                        "text": text,
                        "entities": [
                                [
                                        "offset": 0,
                                        "length": 6,
                                        "type": "bot_command"
                                ]
                        ]
                ]
        ])
    }

    APIGatewayProxyRequestEvent createRequest(Handler handler, Map<String, Object> payload) {
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent()
        ObjectMapper objectMapper = handler.applicationContext.getBean(ObjectMapper)
        String body = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(payload)
        request.body = body
        request.path = '/xxxyyyzzz'
        request.httpMethod = 'POST'
        request
    }
}
