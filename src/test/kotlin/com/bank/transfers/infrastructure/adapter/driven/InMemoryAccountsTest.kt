package com.bank.transfers.infrastructure.adapter.driven

import com.bank.transfers.app.domain.Account
import com.bank.transfers.app.domain.AccountId
import com.bank.transfers.app.domain.CustomerId
import com.bank.transfers.app.port.driven.AccountFinder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class InMemoryAccountsTest {

    @Test
    fun `should return an account by customer when it exists`() {
        val customerId = CustomerId(UUID.randomUUID())
        val account = Account(AccountId(UUID.randomUUID()))
        val accounts:AccountFinder = InMemoryAccounts()
        (accounts as InMemoryAccounts).add(customerId, account)

        assertThat(accounts.find(customerId)).isEqualTo(account)
    }

    @Test
    fun `should not return an account when it does not exists for a customer`() {
      assertThat(InMemoryAccounts().find(CustomerId(UUID.randomUUID()))).isNull()
    }
}
