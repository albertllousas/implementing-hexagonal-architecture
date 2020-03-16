package com.bank.transfers.infrastructure.adapter.driven

import com.bank.transfers.app.domain.Account
import com.bank.transfers.app.domain.CustomerId
import com.bank.transfers.app.port.driven.AccountFinder

class InMemoryAccounts : AccountFinder {
    override fun find(customerId: CustomerId): Account? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
