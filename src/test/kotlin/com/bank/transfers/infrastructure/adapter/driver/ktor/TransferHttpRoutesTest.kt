package com.bank.transfers.infrastructure.adapter.driver.ktor

import com.bank.transfers.app.port.driver.TransferMoney
import com.bank.transfers.app.port.driver.TransferMoneyRequest
import com.bank.transfers.app.port.driver.TransferMoneyResponse
import com.bank.transfers.app.port.driver.TransferMoneyResponse.*
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.routing.Routing
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal.*
import java.util.UUID.*


class TransferHttpRoutesTest {

    private val transferMoneyPort = mockk<TransferMoney>()

    private val objectMapper = ObjectMapper()

    @Test
    fun `should transfer money from one account to another`(): Unit =
        withTestApp {
            val request = TransferHttpRequest(TEN, randomUUID(), randomUUID())
            every { transferMoneyPort(TransferMoneyRequest(request.amount, request.from, request.to)) } returns Success

            val call = handleRequest(HttpMethod.Post, "/transfers") {
                addHeader("Content-Type", "application/json")
                setBody(objectMapper.writeValueAsString(request))
            }

            assertThat(call.response.status()).isEqualTo(HttpStatusCode.Created)
        }

    private fun withTestApp(callback: TestApplicationEngine.() -> Unit): Unit {
        withTestApplication({
            install(ContentNegotiation) { jackson {} }
            install(Routing) {
                transfers(transferMoneyPort)
            }
        } , callback)
    }

}
