package com.codigobot.telegram.rss

import com.microchatbots.telegrambots.awslambda.Handler
import io.micronaut.context.ApplicationContextBuilder

class MockHandler extends Handler {
    Map<String, Object> props

    MockHandler(Map<String, Object> props) {
        this.props = props
    }

    @Override
    ApplicationContextBuilder newApplicationContextBuilder() {
        super.newApplicationContextBuilder().properties(props)
    }
}