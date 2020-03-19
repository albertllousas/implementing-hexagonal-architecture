package com.bank.transfers.app.domain

import com.bank.transfers.app.domain.Transaction.Credit
import com.bank.transfers.app.domain.Transaction.Debit
import java.math.BigDecimal

// we can not use a data class, if we make the constructor private, it is still exposed through copy method
class Transfer private constructor(
    val debit: Debit,
    val credit: Credit
) {
    companion object {
        fun create(
            from: Account,
            to: Account,
            amount: BigDecimal
        ): Transfer =
            Transfer(
                Debit(amount, from.accountId),
                Credit(amount, to.accountId)
            )
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Transfer

        if (debit != other.debit) return false
        if (credit != other.credit) return false

        return true
    }

    override fun hashCode(): Int {
        var result = debit.hashCode()
        result = 31 * result + credit.hashCode()
        return result
    }


}

fun Transfer.Companion.isValid(amount: BigDecimal): Boolean = amount >= BigDecimal.ONE




