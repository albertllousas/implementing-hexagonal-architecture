package com.bank.transfers.app.domain

import com.bank.transfers.app.domain.Transaction.Credit
import com.bank.transfers.app.domain.Transaction.Debit
import java.math.BigDecimal
import java.math.BigDecimal.ZERO

sealed class Transaction {
    abstract val amount: BigDecimal
    abstract val accountId: AccountId

    data class Debit(
        override val amount: BigDecimal,
        override val accountId: AccountId
    ) : Transaction()

    data class Credit(
        override val amount: BigDecimal,
        override val accountId: AccountId
    ) : Transaction()
}

fun List<Transaction>.balance(): BigDecimal = this.foldRight(
    initial = ZERO,
    operation = { transaction, acc ->
        when (transaction) {
            is Debit -> acc.subtract(transaction.amount)
            is Credit -> acc.plus(transaction.amount)
        }
    })
