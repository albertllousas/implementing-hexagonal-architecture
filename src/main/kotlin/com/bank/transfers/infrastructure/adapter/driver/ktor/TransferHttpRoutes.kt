package com.bank.transfers.infrastructure.adapter.driver.ktor

import com.bank.transfers.app.port.driver.TransferMoney
import com.bank.transfers.app.port.driver.TransferMoneyRequest
import com.bank.transfers.app.port.driver.TransferMoneyResponse
import com.bank.transfers.app.port.driver.TransferMoneyResponse.*
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route
import java.math.BigDecimal
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import java.util.UUID

fun Route.transfers(transferMoneyPort: TransferMoney) = route("/transfers") {
    post {
        val request = call.receive<TransferHttpRequest>()
        val response = transferMoneyPort.invoke(TransferMoneyRequest(request.amount, request.from, request.to))
        when(response) {
            is Success -> call.respond(Created)
            is UserNotFound -> call.respond(NotFound, Error("User '${response.userId}' not found"))
            is NotEnoughFounds -> call.respond(BadRequest, Error("Not enough founds"))
        }.exhaustive
    }
}

val Any?.exhaustive get() = Unit

data class TransferHttpRequest(val amount: BigDecimal, val from: UUID, val to: UUID)

data class Error(val message: String)
