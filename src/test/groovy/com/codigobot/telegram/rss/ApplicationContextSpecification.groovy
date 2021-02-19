package com.codigobot.telegram.rss

import com.microchatbots.telegrambots.awslambda.Handler
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

abstract class ApplicationContextSpecification extends Specification implements ApiGatewayUtils {

    @Shared
    @AutoCleanup
    Handler handler = new Handler()
}
