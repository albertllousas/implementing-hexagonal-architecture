package com.bank.transfers.app.domain

import java.math.BigDecimal

sealed class Transaction {
    data class Debit(val amount: BigDecimal, val accountId: AccountId): Transaction()
    data class Credit(val amount: BigDecimal, val accountId: AccountId): Transaction()
}
