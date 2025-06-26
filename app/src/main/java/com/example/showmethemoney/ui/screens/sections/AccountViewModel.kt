package com.example.showmethemoney.ui.screens.sections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.ApiResult
import com.example.domain.model.AccountHistoryDomain
import com.example.domain.usecase.GetAccountHistoryUseCase
import com.example.showmethemoney.ui.utils.AccountManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AccountViewModel @Inject constructor(
    private val getAccountHistoryUseCase: GetAccountHistoryUseCase
) : ViewModel() {

    private val _accountHistory = MutableStateFlow<ApiResult<AccountHistoryDomain>>(ApiResult.Loading)
    val accountHistory: StateFlow<ApiResult<AccountHistoryDomain>> = _accountHistory

    fun loadAccountHistory() {
        viewModelScope.launch {
            val accountId = AccountManager.selectedAccountId.toString()

            _accountHistory.value = ApiResult.Loading
            when (val result = getAccountHistoryUseCase.execute(accountId)) {
                is ApiResult.Success -> {
                    _accountHistory.value = ApiResult.Success(result.data)
                }
                is ApiResult.Error -> {
                    _accountHistory.value = result
                }
                ApiResult.Loading -> Unit
            }
        }
    }
}
