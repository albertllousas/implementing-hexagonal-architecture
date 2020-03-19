package com.bank.transfers.app.domain

import com.bank.transfers.app.domain.Transaction.Credit
import com.bank.transfers.app.domain.Transaction.Debit
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.BigDecimal.ONE
import java.math.BigDecimal.TEN
import java.util.UUID

class TransactionTest {
    @Test
    fun `should calculate the balance given a list of transactions`() {
        val accountId = AccountId(UUID.randomUUID())

        val balance = listOf(
            Credit(TEN, accountId), Credit(TEN, accountId), Debit(TEN, accountId), Debit(ONE, accountId)
        ).balance()

        assertThat(balance).isEqualTo(BigDecimal(9))
    }
}
