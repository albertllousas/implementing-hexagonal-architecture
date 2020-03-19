package com.bank.transfers.app.port.driven

import com.bank.transfers.app.domain.AccountId
import java.math.BigDecimal

interface AccountBalanceChecker {
    fun getBalance(accountId: AccountId): BigDecimal?
}
