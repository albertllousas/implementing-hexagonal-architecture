package com.bank.transfers.app.port.driver

import java.math.BigDecimal
import java.util.UUID

interface TransferMoney {
    operator fun invoke(request: TransferMoneyRequest): TransferMoneyResponse
}

data class TransferMoneyRequest(
    val amount: BigDecimal,
    val from: UUID,
    val to: UUID
)

sealed class TransferMoneyResponse {
    object Success : TransferMoneyResponse()
    data class UserNotFound(val userId: UUID) : TransferMoneyResponse()
    data class NotEnoughFounds(val userId: UUID) : TransferMoneyResponse()
}

