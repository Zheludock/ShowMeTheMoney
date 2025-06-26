package com.example.showmethemoney.ui.screens.sections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.ApiResult
import com.example.domain.model.AccountHistoryDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AccountViewModel @Inject constructor(
) : ViewModel() {

    private val _accountHistory = MutableStateFlow<ApiResult<AccountHistoryDomain>>(ApiResult.Loading)
    val accountHistory: StateFlow<ApiResult<AccountHistoryDomain>> = _accountHistory

    fun loadAccountHistory() {
        viewModelScope.launch {
            //val accountId = accountManager.selectedAccountId

            _accountHistory.value = ApiResult.Loading
            //_accountHistory.value = useCase
        }
    }
}
