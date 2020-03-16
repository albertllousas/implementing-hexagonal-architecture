package com.bank.transfers.infrastructure.config

import io.ktor.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main(args: Array<String>): Unit {
    runServer(Application::module)
}

fun runServer(
    appModule: (Application) -> Unit,
    port: Int = 8080
) =
    embeddedServer(
        factory = Netty,
        port = port,
        module = appModule
    ).start(wait = true)
