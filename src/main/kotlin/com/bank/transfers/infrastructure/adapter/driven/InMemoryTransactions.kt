package com.bank.transfers.infrastructure.adapter.driven

import com.bank.transfers.app.domain.AccountId
import com.bank.transfers.app.domain.Transaction
import com.bank.transfers.app.domain.Transfer
import com.bank.transfers.app.domain.balance
import com.bank.transfers.app.port.driven.AccountBalanceChecker
import com.bank.transfers.app.port.driven.Transactor
import com.bank.transfers.app.port.driven.TransactorResult
import java.math.BigDecimal
import java.math.BigDecimal.ZERO

class InMemoryTransactions(
    previousTransactions: List<Transaction> = emptyList()
) : Transactor, AccountBalanceChecker {

    override fun getBalance(accountId: AccountId): BigDecimal? = transactions[accountId]?.balance()

    private val transactions = mutableMapOf<AccountId, List<Transaction>>()

    init {
        val groupByAccount = previousTransactions.groupBy { transaction -> transaction.accountId }
        transactions.putAll(groupByAccount)
    }

    override fun execute(transfer: Transfer): TransactorResult {
        val accountToDebitBalance = transactions[transfer.debit.accountId]?.balance() ?: ZERO
        if (accountToDebitBalance < transfer.debit.amount) return TransactorResult.NotEnoughFounds
        else {
            transactions.computeIfPresent(transfer.debit.accountId) { _, txs -> txs + listOf(transfer.debit) }
            transactions.compute(transfer.credit.accountId) { _, txs ->
                (txs ?: emptyList<Transaction>()) + listOf(transfer.credit)
            }
        }
        return TransactorResult.Success
    }

}
