package com.bank.transfers.infrastructure.config

import com.bank.transfers.app.usecase.TransferMoneyUseCase
import com.bank.transfers.infrastructure.adapter.driver.ktor.transfers
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.jackson.jackson
import io.ktor.routing.Routing
import org.slf4j.event.Level
import org.slf4j.event.Level.*

fun Application.module() {
    install(DefaultHeaders)
    install(CallLogging) {
        level = INFO
    }
    install(ContentNegotiation) { jackson {} }
    install(Routing) {
        transfers(TransferMoneyUseCase())
    }
}
