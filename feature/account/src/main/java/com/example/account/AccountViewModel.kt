package com.example.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.account.editaccount.toAccountDetailsItem
import com.example.domain.response.ApiResult
import com.example.domain.usecase.GetAccountDetailsUseCase
import com.example.ui.AccountManager
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
) : ViewModel() {
    val accountId = AccountManager.selectedAccountId

    private val _accountDetails =
        MutableStateFlow<ApiResult<AccountDetailsItem>>(ApiResult.Loading)
    val accountDetails: StateFlow<ApiResult<AccountDetailsItem>> = _accountDetails

    fun loadAccountDetails() {
        viewModelScope.launch {
            _accountDetails.value = ApiResult.Loading
            when (val result = getAccountDetailsUseCase.execute(accountId)) {
                is ApiResult.Success -> {
                    _accountDetails.value = ApiResult.Success(result.data.toAccountDetailsItem())
                    AccountManager.updateAcc(result.data.currency,
                        result.data.name)
                }
                is ApiResult.Error -> {
                    _accountDetails.value = result
                }
                ApiResult.Loading -> Unit
            }
        }
    }
}