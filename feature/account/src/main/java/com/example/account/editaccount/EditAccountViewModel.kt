package com.example.account.editaccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.account.AccountDetailsItem
import com.example.domain.model.AccountDetailsDomain
import com.example.domain.model.AccountDomain
import com.example.domain.response.ApiResult
import com.example.domain.usecase.account.GetAccountDetailsUseCase
import com.example.domain.usecase.account.UpdateAccountUseCase
import com.example.utils.AccountManager
import kotlinx.coroutines.Dispatchers
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

    private val _accountDetails = MutableStateFlow<AccountDetailsItem?>(null)
    val accountDetails: StateFlow<AccountDetailsItem?> = _accountDetails

    fun loadAccountDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            _accountDetails.value = getAccountDetailsUseCase.execute(accountId).toAccountDetailsItem()
        }
    }

    fun updateAccount(
        name: String,
        currency: String,
        balance: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _accountDetails.value = updateAccountUseCase.execute(
                id = accountId,
                currency = currency,
                name = name,
                balance = balance
            ).toAccountDetailsItem()
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


fun AccountDetailsDomain.toAccountDetailsItem(): AccountDetailsItem {
    return AccountDetailsItem(
        id = id,
        name = name,
        balance = balance,
        currency = currency,
    )
}

fun AccountDomain.toAccountDetailsItem(): AccountDetailsItem {
    return AccountDetailsItem(
        id = id,
        name = name,
        balance = balance,
        currency = currency,
    )
}