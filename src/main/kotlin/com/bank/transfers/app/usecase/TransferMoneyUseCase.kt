package com.bank.transfers.app.usecase

import com.bank.transfers.app.domain.Account
import com.bank.transfers.app.domain.CustomerId
import com.bank.transfers.app.domain.Transfer
import com.bank.transfers.app.domain.isValid
import com.bank.transfers.app.port.driven.AccountFinder
import com.bank.transfers.app.port.driven.Transactor
import com.bank.transfers.app.port.driven.TransactorResult
import com.bank.transfers.app.port.driver.TransferMoney
import com.bank.transfers.app.port.driver.TransferMoneyRequest
import com.bank.transfers.app.port.driver.TransferMoneyResponse
import com.bank.transfers.app.port.driver.TransferMoneyResponse.AccountNotFound
import com.bank.transfers.app.port.driver.TransferMoneyResponse.InvalidAmount
import com.bank.transfers.app.port.driver.TransferMoneyResponse.NotEnoughFounds
import com.bank.transfers.app.port.driver.TransferMoneyResponse.Success
import java.math.BigDecimal

class TransferMoneyUseCase(
    private val accountFinder: AccountFinder,
    private val transactor: Transactor,
    private val createTransfer: (Account, Account, BigDecimal) -> Transfer = Transfer.Companion::create,
    private val isValidAmountForTransfer: (BigDecimal) -> Boolean = Transfer.Companion::isValid
) : TransferMoney {

    override fun invoke(request: TransferMoneyRequest): TransferMoneyResponse {
        if (!isValidAmountForTransfer(request.amount)) return InvalidAmount
        val from = accountFinder.find(CustomerId(request.from)) ?: return AccountNotFound(request.from)
        val to = accountFinder.find(CustomerId(request.to)) ?: return AccountNotFound(request.to)
        val transfer = createTransfer(from, to, request.amount)
        return when (transactor.execute(transfer)) {
            is TransactorResult.Success -> Success
            is TransactorResult.NotEnoughFounds -> NotEnoughFounds
        }
    }
}
