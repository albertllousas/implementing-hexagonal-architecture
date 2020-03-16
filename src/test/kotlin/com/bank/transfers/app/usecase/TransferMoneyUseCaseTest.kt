package com.bank.transfers.app.usecase

import com.bank.transfers.app.domain.Account
import com.bank.transfers.app.domain.AccountId
import com.bank.transfers.app.domain.CustomerId
import com.bank.transfers.app.domain.Transaction
import com.bank.transfers.app.domain.Transfer
import com.bank.transfers.app.port.driven.AccountFinder
import com.bank.transfers.app.port.driven.Transactor
import com.bank.transfers.app.port.driven.TransactorResult
import com.bank.transfers.app.port.driver.TransferMoneyRequest
import com.bank.transfers.app.port.driver.TransferMoneyResponse
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.UUID

class TransferMoneyUseCaseTest {

    private val transactor = mockk<Transactor>()

    private val accountFinder = mockk<AccountFinder>()

    private val createTransfer = mockk<(Account, Account, BigDecimal) -> Transfer>()

    private val transferMoneyUseCase = TransferMoneyUseCase(accountFinder, transactor, createTransfer)

    @Test
    fun `should transfer money successfully`() {
        val request = TransferMoneyRequest(BigDecimal.TEN, UUID.randomUUID(), UUID.randomUUID())
        val accountFrom = Account(AccountId(UUID.randomUUID()))
        val accountTo = Account(AccountId(UUID.randomUUID()))
        val transfer = Transfer(
            Transaction.Debit(request.amount.negate(), accountFrom.accountId),
            Transaction.Credit(request.amount, accountTo.accountId)
        )
        every { accountFinder.find(CustomerId(request.from)) } returns accountFrom
        every { accountFinder.find(CustomerId(request.to)) } returns accountTo
        every { createTransfer(accountFrom, accountTo, request.amount) } returns transfer
        every { transactor.execute(transfer) } returns TransactorResult.Success

        val response = transferMoneyUseCase.invoke(request)

        assertThat(response).isEqualTo(TransferMoneyResponse.Success)
    }

    @Test
    fun `should fail transferring money when amount is invalid`() {
        val request = TransferMoneyRequest(BigDecimal.ZERO, UUID.randomUUID(), UUID.randomUUID())

        val response = transferMoneyUseCase.invoke(request)

        assertThat(response).isEqualTo(TransferMoneyResponse.InvalidAmount)
    }

    @Test
    fun `should fail transferring money when the customer to debit has no account`() {
        val request = TransferMoneyRequest(BigDecimal.TEN, UUID.randomUUID(), UUID.randomUUID())
        every { accountFinder.find(CustomerId(request.from)) } returns null

        val response = transferMoneyUseCase.invoke(request)

        assertThat(response).isEqualTo(TransferMoneyResponse.AccountNotFound(request.from))
    }

    @Test
    fun `should fail transferring money when the customer to credit has no account`() {
        val request = TransferMoneyRequest(BigDecimal.TEN, UUID.randomUUID(), UUID.randomUUID())
        val accountFrom = Account(AccountId(UUID.randomUUID()))
        every { accountFinder.find(CustomerId(request.from)) } returns accountFrom
        every { accountFinder.find(CustomerId(request.to)) } returns null

        val response = transferMoneyUseCase.invoke(request)

        assertThat(response).isEqualTo(TransferMoneyResponse.AccountNotFound(request.to))
    }

    @Test
    fun `should fail transferring money when customer to debit has not enough money`() {
        val request = TransferMoneyRequest(BigDecimal.TEN, UUID.randomUUID(), UUID.randomUUID())
        val accountFrom = Account(AccountId(UUID.randomUUID()))
        val accountTo = Account(AccountId(UUID.randomUUID()))
        val transfer = Transfer(
            Transaction.Debit(request.amount.negate(), accountFrom.accountId),
            Transaction.Credit(request.amount, accountTo.accountId)
        )
        every { accountFinder.find(CustomerId(request.from)) } returns accountFrom
        every { accountFinder.find(CustomerId(request.to)) } returns accountTo
        every { createTransfer(accountFrom, accountTo, request.amount) } returns transfer
        every { transactor.execute(transfer) } returns TransactorResult.NotEnoughFounds

        val response = transferMoneyUseCase.invoke(request)

        assertThat(response).isEqualTo(TransferMoneyResponse.NotEnoughFounds)
    }

}
