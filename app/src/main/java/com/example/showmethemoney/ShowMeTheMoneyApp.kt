package com.example.showmethemoney

import android.app.Application
import android.util.Log
import com.example.showmethemoney.data.AccountManager
import com.example.showmethemoney.data.FinanceApiService
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class ShowMeTheMoneyApp : Application() {
    @Inject
    lateinit var accountManager: AccountManager
    @Inject lateinit var apiService: FinanceApiService

    override fun onCreate() {
        super.onCreate()
        loadInitialAccount()
    }

    private fun loadInitialAccount() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val accounts = apiService.getAccounts()
                if (accounts.isNotEmpty()) {
                    accountManager.selectedAccountId = accounts.first().id
                    Log.d("FinanceApp", "Selected account ID: ${accountManager.selectedAccountId}, total count: ${accounts.size}")
                }
            } catch (e: Exception) {
                Log.e("FinanceApp", "Error loading accounts", e)
            }
        }
    }
}