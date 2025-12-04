package ru.ekataskin.booktracker.app.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.ekataskin.booktracker.common.RequestV1Processor
import ru.ekataskin.booktracker.common.interfaces.IDomain
import ru.ekataskin.booktracker.common.interfaces.IRequestProcessor
import ru.ekataskin.booktracker.domain.Domain

@Configuration
class AppConfig {
    @Bean
    fun domain(): IDomain = Domain()

    @Bean
    fun requestProcessor(domain: IDomain): IRequestProcessor = RequestV1Processor(domain)
}