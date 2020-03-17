package com.bank.transfers.infrastructure.adapter.driven

import com.bank.transfers.app.domain.Account
import com.bank.transfers.app.domain.CustomerId
import com.bank.transfers.app.port.driven.AccountFinder

class InMemoryAccounts : AccountFinder {

    private val accounts = mutableMapOf<CustomerId, Account>()

    override fun find(customerId: CustomerId): Account? = accounts.get(customerId)

    fun add(customerId: CustomerId, account: Account) = accounts.put(customerId, account)
}
