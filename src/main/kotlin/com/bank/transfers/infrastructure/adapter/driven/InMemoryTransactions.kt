package com.bank.transfers.infrastructure.adapter.driven

import com.bank.transfers.app.domain.Transfer
import com.bank.transfers.app.port.driven.Transactor
import com.bank.transfers.app.port.driven.TransactorResult

class InMemoryTransactions: Transactor {
    override fun execute(transfer: Transfer): TransactorResult {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
