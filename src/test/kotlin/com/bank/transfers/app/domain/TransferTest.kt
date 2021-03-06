package com.bank.transfers.app.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal.TEN
import java.math.BigDecimal.ZERO
import java.math.BigDecimal.valueOf
import java.util.UUID


class TransferTest {

    @Test
    fun `should create a transfer`() {
        val accountFrom = Account(AccountId(UUID.randomUUID()))
        val accountTo = Account(AccountId(UUID.randomUUID()))

        val transfer = Transfer.create(accountFrom, accountTo, TEN)

        assertThat(transfer.credit).isEqualTo(Transaction.Credit(TEN, accountTo.accountId))
        assertThat(transfer.debit).isEqualTo(Transaction.Debit(TEN, accountFrom.accountId))
    }

    @Test
    fun `should invalidate an amount zero for a transfer`() {
        assertThat(Transfer.isValid(ZERO)).isFalse()
    }

    @Test
    fun `should invalidate a negative amount zero for a transfer`() {
        assertThat(Transfer.isValid(valueOf(-1))).isFalse()
    }

    @Test
    fun `should validate a positive amount for a transfer`() {
        assertThat(Transfer.isValid(valueOf(1))).isTrue()
    }
}
