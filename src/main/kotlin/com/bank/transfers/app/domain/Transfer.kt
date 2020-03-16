package com.bank.transfers.app.domain

import com.bank.transfers.app.domain.Transaction.*
import java.math.BigDecimal

data class Transfer(
    val debit: Debit,
    val credit: Credit
) {
    companion object
}

fun Transfer.Companion.create(
    from: Account,
    to: Account,
    amount: BigDecimal
): Transfer =
    Transfer(
        Debit(amount.negate(), from.accountId),
        Credit(amount, to.accountId)
    )

fun Transfer.Companion.isValid(amount: BigDecimal): Boolean = amount >= BigDecimal.ONE




