package com.bank.transfers.infrastructure.adapter.driven

import com.bank.transfers.app.domain.Account
import com.bank.transfers.app.domain.AccountId
import com.bank.transfers.app.domain.Transaction.Credit
import com.bank.transfers.app.domain.Transfer
import com.bank.transfers.app.port.driven.TransactorResult.NotEnoughFounds
import com.bank.transfers.app.port.driven.TransactorResult.Success
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.BigDecimal.ONE
import java.math.BigDecimal.TEN
import java.util.UUID


internal class InMemoryTransactionsTest {

    @Test
    fun `should execute a successful transfer between two existant accounts`() {
        val accountFrom = AccountId(UUID.randomUUID())
        val accountTo = AccountId(UUID.randomUUID())
        val transactor = InMemoryTransactions(listOf(Credit(TEN, accountFrom), Credit(ONE, accountTo)))

        val result = transactor.execute(transfer = Transfer.create(Account(accountFrom), Account(accountTo), ONE))

        assertThat(result).isEqualTo(Success)
        // check side effects
        assertThat(transactor.getBalance(accountFrom)).isEqualTo(BigDecimal(9))
        assertThat(transactor.getBalance(accountTo)).isEqualTo(BigDecimal(2))
    }

    @Test
    fun `should execute a successful transfer and initialize the account to credit is not present`() {
        val accountFrom = AccountId(UUID.randomUUID())
        val accountTo = AccountId(UUID.randomUUID())
        val transactor = InMemoryTransactions(listOf(Credit(TEN, accountFrom)))

        val result = transactor.execute(transfer = Transfer.create(Account(accountFrom), Account(accountTo), ONE))

        assertThat(result).isEqualTo(Success)
        // check side effects
        assertThat(transactor.getBalance(accountFrom)).isEqualTo(BigDecimal(9))
        assertThat(transactor.getBalance(accountTo)).isEqualTo(BigDecimal(1))
    }

    @Test
    fun `should fail when account to debit have not enough founds`() {
        val accountFrom = AccountId(UUID.randomUUID())
        val accountTo = AccountId(UUID.randomUUID())
        val transactor = InMemoryTransactions(listOf(Credit(ONE, accountFrom)))

        val result = transactor.execute(transfer = Transfer.create(Account(accountFrom), Account(accountTo), TEN))

        assertThat(result).isEqualTo(NotEnoughFounds)
    }

    @Test
    fun `should fail with not enough found when account to credit is not present`() {
        val accountFrom = AccountId(UUID.randomUUID())
        val accountTo = AccountId(UUID.randomUUID())
        val transactor = InMemoryTransactions()

        val result = transactor.execute(transfer = Transfer.create(Account(accountFrom), Account(accountTo), ONE))

        assertThat(result).isEqualTo(NotEnoughFounds)
    }

}
