package com.bank.transfers.app.domain

import java.math.BigDecimal

data class Transfer(val debit: Transaction, val credit: Transaction) {
   companion object
}

fun Transfer.Companion.create(from: Account, to: Account, amount: BigDecimal): Transfer {
    TODO()
}



