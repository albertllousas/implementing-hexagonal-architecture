package com.bank.transfers.infrastructure.adapter.driver.ktor

import com.bank.transfers.app.port.driver.TransferMoney
import com.bank.transfers.app.port.driver.TransferMoneyRequest
import com.bank.transfers.app.port.driver.TransferMoneyResponse
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route
import java.math.BigDecimal
import java.util.UUID

fun Route.transfers(transferMoneyPort: TransferMoney) = route("/transfers") {
    post {
        val request = call.receive<TransferHttpRequest>()
        when(transferMoneyPort.invoke(TransferMoneyRequest(request.amount, request.from, request.to))) {
            TransferMoneyResponse.Success -> call.respond(HttpStatusCode.Created)
        }
    }
}

data class TransferHttpRequest(val amount: BigDecimal, val from: UUID, val to: UUID)
