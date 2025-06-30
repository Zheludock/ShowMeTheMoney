package com.example.showmethemoney.ui.screens.account

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.response.ApiResult
import com.example.domain.usecase.GetAccountDetailsUseCase
import com.example.domain.usecase.UpdateAccountUseCase
import com.example.showmethemoney.ui.utils.AccountManager
import com.example.showmethemoney.ui.utils.toAccountDetailsItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для работы с данными счета:
 * - Загрузка истории счета
 * - Управление состоянием загрузки
 * - Предоставление данных UI-слою
 *
 * @property accountDetails StateFlow с результатом загрузки (Loading/Success/Error)
 * @param getAccountDetailsUseCase Use case для получения истории счета
 */
class AccountViewModel @Inject constructor(
    private val getAccountDetailsUseCase: GetAccountDetailsUseCase,
    private val updateAccountUseCase: UpdateAccountUseCase,
) : ViewModel() {
    val accountId = AccountManager.selectedAccountId

    var accountName by mutableStateOf(AccountManager.selectedAccountName)
        private set

    var accountCurrency by mutableStateOf(AccountManager.selectedAccountCurrency)
        private set

    private val _accountDetails =
        MutableStateFlow<ApiResult<AccountDetailsItem>>(ApiResult.Loading)
    val accountDetails: StateFlow<ApiResult<AccountDetailsItem>> = _accountDetails

    fun loadAccountDetails() {
        viewModelScope.launch {
            _accountDetails.value = ApiResult.Loading
            when (val result = getAccountDetailsUseCase.execute(accountId)) {
                is ApiResult.Success -> {
                    _accountDetails.value = ApiResult.Success(result.data.toAccountDetailsItem())
                    accountName = result.data.name
                    accountCurrency = result.data.currency
                }
                is ApiResult.Error -> {
                    _accountDetails.value = result
                }
                ApiResult.Loading -> Unit
            }
        }
    }

    private suspend fun updateAccount(
        name: String,
        currency: String,
        balance: String
    ) {
        val currentDetails = (_accountDetails.value as? ApiResult.Success)?.data ?: return

        val originalDetails = currentDetails.copy()

        _accountDetails.value = ApiResult.Loading

        when (val result = updateAccountUseCase.execute(
            id = accountId,
            currency = currency,
            name = name,
            balance = balance
        )) {
            is ApiResult.Success -> {
                val updatedItem = result.data.toAccountDetailsItem()
                _accountDetails.value = ApiResult.Success(updatedItem)
                accountName = updatedItem.name
                accountCurrency = updatedItem.currency

                AccountManager.selectedAccountName = updatedItem.name
                AccountManager.selectedAccountCurrency = updatedItem.currency
            }
            is ApiResult.Error -> {
                _accountDetails.value = ApiResult.Success(originalDetails)
            }
            ApiResult.Loading -> Unit
        }
    }

    fun updateCurrency(currency: String, balance: String) {
        viewModelScope.launch {
            updateAccount(accountName, currency, balance)
        }
    }

    fun updateName(name: String, balance: String) {
        viewModelScope.launch {
            updateAccount(name, accountCurrency, balance)
        }
    }
}