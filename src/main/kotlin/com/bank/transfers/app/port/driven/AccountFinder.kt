package com.bank.transfers.app.port.driven

import com.bank.transfers.app.domain.Account
import com.bank.transfers.app.domain.CustomerId

interface AccountFinder {
    fun find(customerId: CustomerId) : Account?
}
