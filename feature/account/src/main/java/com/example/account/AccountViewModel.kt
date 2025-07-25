package com.example.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.account.editaccount.toAccountDetailsItem
import com.example.domain.response.ApiResult
import com.example.domain.usecase.account.GetAccountDetailsUseCase
import com.example.utils.AccountManager
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

    private val _accountDetails = MutableStateFlow<AccountDetailsItem?>(null)
    val accountDetails: StateFlow<AccountDetailsItem?> = _accountDetails

    fun loadAccountDetails() {
        viewModelScope.launch {
            _accountDetails.value = getAccountDetailsUseCase.execute(accountId).toAccountDetailsItem()
            accountDetails.value?.currency?.let {
                accountDetails.value?.name?.let { newName ->
                    AccountManager.updateAcc(
                        it,
                        newName
                    )
                }
            }
        }
    }
}