package com.bank.transfers.infrastructure.adapter.driver.ktor

import com.bank.transfers.app.port.driver.TransferMoney
import com.bank.transfers.app.port.driver.TransferMoneyRequest
import com.bank.transfers.app.port.driver.TransferMoneyResponse.NotEnoughFounds
import com.bank.transfers.app.port.driver.TransferMoneyResponse.Success
import com.bank.transfers.app.port.driver.TransferMoneyResponse.UserNotFound
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
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
import java.math.BigDecimal.TEN
import java.util.UUID.randomUUID


class TransferHttpRoutesTest {

    private val transferMoneyPort = mockk<TransferMoney>()

    private val objectMapper = jacksonObjectMapper()

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

    @Test
    fun `should not transfer money when one of the users is not in the system`(): Unit =
        withTestApp {
            val request = TransferHttpRequest(TEN, randomUUID(), randomUUID())
            every {
                transferMoneyPort(TransferMoneyRequest(request.amount, request.from, request.to))
            } returns UserNotFound(request.from)

            val call = handleRequest(HttpMethod.Post, "/transfers") {
                addHeader("Content-Type", "application/json")
                setBody(objectMapper.writeValueAsString(request))
            }

            assertThat(call.response.status()).isEqualTo(HttpStatusCode.NotFound)
            assertThat(objectMapper.readValue<Error>(call.response.content?:""))
                .isEqualTo(Error("User '${request.from}' not found"))
        }

    @Test
    fun `should not transfer money when there is not enough money`(): Unit =
        withTestApp {
            val request = TransferHttpRequest(TEN, randomUUID(), randomUUID())
            every {
                transferMoneyPort(TransferMoneyRequest(request.amount, request.from, request.to))
            } returns NotEnoughFounds(request.from)

            val call = handleRequest(HttpMethod.Post, "/transfers") {
                addHeader("Content-Type", "application/json")
                setBody(objectMapper.writeValueAsString(request))
            }

            assertThat(call.response.status()).isEqualTo(HttpStatusCode.BadRequest)
            assertThat(objectMapper.readValue<Error>(call.response.content?:""))
                .isEqualTo(Error("Not enough founds"))
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
