package com.example.showmethemoney.ui.screens.account.editaccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.response.ApiResult
import com.example.domain.usecase.GetAccountDetailsUseCase
import com.example.domain.usecase.UpdateAccountUseCase
import com.example.showmethemoney.ui.screens.account.AccountDetailsItem
import com.example.showmethemoney.ui.utils.AccountManager
import com.example.showmethemoney.ui.utils.toAccountDetailsItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
/**
 * ViewModel для экрана редактирования аккаунта.
 *
 * Отвечает за:
 * - Загрузку текущих данных аккаунта
 * - Валидацию вводимых данных
 * - Обновление информации об аккаунте
 * - Управление состоянием экрана
 *
 * @property accountId ID текущего выбранного аккаунта (берется из AccountManager)
 * @property accountDetails StateFlow с состоянием загрузки/обновления данных аккаунта
 *
 * @param updateAccountUseCase UseCase для обновления данных аккаунта
 * @param getAccountDetailsUseCase UseCase для получения деталей аккаунта
 */
class EditAccountViewModel @Inject constructor(
    private val updateAccountUseCase: UpdateAccountUseCase,
    private val getAccountDetailsUseCase: GetAccountDetailsUseCase
) : ViewModel() {
    val accountId = AccountManager.selectedAccountId

    private val _accountDetails = MutableStateFlow<ApiResult<AccountDetailsItem>>(ApiResult.Loading)
    val accountDetails: StateFlow<ApiResult<AccountDetailsItem>> = _accountDetails

    fun loadAccountDetails() {
        viewModelScope.launch {
            _accountDetails.value = ApiResult.Loading
            when (val result = getAccountDetailsUseCase.execute(accountId)) {
                is ApiResult.Success -> {
                    _accountDetails.value = ApiResult.Success(result.data.toAccountDetailsItem())
                }

                is ApiResult.Error -> {
                    _accountDetails.value = result
                }

                ApiResult.Loading -> Unit
            }
        }
    }

    fun updateAccount(
        name: String,
        currency: String,
        balance: String
    ) {
        viewModelScope.launch {
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

                    AccountManager.updateAcc(updatedItem.currency, updatedItem.name)
                }

                is ApiResult.Error -> {
                    _accountDetails.value = result
                }

                ApiResult.Loading -> Unit
            }
        }
    }

    fun isValidBalance(input: String): Boolean {
        return try {
            input.replace(",", ".").toBigDecimal()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }
}