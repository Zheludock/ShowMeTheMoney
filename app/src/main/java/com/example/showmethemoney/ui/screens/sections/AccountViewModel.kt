package com.example.showmethemoney.ui.screens.sections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.showmethemoney.data.AccountManager
import com.example.showmethemoney.data.FinanceRepository
import com.example.showmethemoney.data.dto.account.toDomain
import com.example.showmethemoney.data.safecaller.ApiCallHelper
import com.example.showmethemoney.data.safecaller.ApiError
import com.example.showmethemoney.data.safecaller.ApiResult
import com.example.showmethemoney.domain.AccountHistoryDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repository: FinanceRepository,
    private val apiCallHelper: ApiCallHelper,
    private val accountManager: AccountManager
) : ViewModel() {

    private val _accountHistory = MutableStateFlow<ApiResult<AccountHistoryDomain>>(ApiResult.Loading)
    val accountHistory: StateFlow<ApiResult<AccountHistoryDomain>> = _accountHistory

    fun loadAccountHistory() {
        viewModelScope.launch {
            val accountId = accountManager.selectedAccountId
            if (accountId == -1) {
                _accountHistory.value = ApiResult.Error(ApiError.UnknownError("Account not selected"))
                return@launch
            }

            _accountHistory.value = ApiResult.Loading
            _accountHistory.value = apiCallHelper.safeApiCall(
                block = {
                    repository.getAccountHistory(accountId).toDomain()
                },
            )
        }
    }
}
