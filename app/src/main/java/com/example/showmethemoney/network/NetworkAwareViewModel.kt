package com.example.showmethemoney.network

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.ApiResult
import com.example.domain.usecase.GetAccountsUseCase
import com.example.showmethemoney.ui.utils.AccountManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NetworkAwareViewModel @Inject constructor(
    networkMonitor: AndroidNetworkMonitor,
    private val getAccountsUseCase: GetAccountsUseCase
) : ViewModel() {

    init {
        loadInitialAccount()
    }

    val isOnline = networkMonitor.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )


    private fun loadInitialAccount() {
        viewModelScope.launch {
            try {
                val result = getAccountsUseCase.execute()
                when (result) {
                    is ApiResult.Success -> {
                        val accounts = result.data
                        if (accounts.isNotEmpty()) {
                            withContext(Dispatchers.Main) {
                                AccountManager.selectedAccountId = accounts.first().id.toInt()
                                Log.d("АККАУНТ","Переписали номер аккаунта на ${AccountManager.selectedAccountId}")
                            }
                            Log.d("FinanceApp",
                                "Selected account ID: ${accounts.first().id}, total count: ${accounts.size}")
                        }
                    }
                    is ApiResult.Error -> {
                        Log.e("FinanceApp", "Error loading accounts: $result")
                    }
                    ApiResult.Loading -> null
                }
            } catch (e: Exception) {
                Log.e("FinanceApp", "Exception loading accounts", e)
            }
        }
    }
}