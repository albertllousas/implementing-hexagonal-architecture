package com.bank.transfers.app.port.driven

import com.bank.transfers.app.domain.Transfer

interface Transactor {
    fun execute(transfer: Transfer) : TransactorResult
}

sealed class TransactorResult {
    object Success: TransactorResult()
    object NotEnoughFounds: TransactorResult()
}
