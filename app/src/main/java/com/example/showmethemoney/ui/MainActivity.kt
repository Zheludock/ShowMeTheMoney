package com.example.showmethemoney.ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.domain.ApiResult
import com.example.domain.usecase.GetAccountsUseCase
import com.example.showmethemoney.ShowMeTheMoneyApp
import com.example.showmethemoney.ui.components.SplashScreen
import com.example.showmethemoney.ui.screens.MainScreen
import com.example.showmethemoney.ui.theme.BackgroundMainColor
import com.example.showmethemoney.ui.theme.ShowMeTheMoneyTheme
import com.example.showmethemoney.ui.utils.AccountManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var getAccountsUseCase: GetAccountsUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as ShowMeTheMoneyApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        loadInitialAccount(getAccountsUseCase)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        enableEdgeToEdge()

        setContent {
            ShowMeTheMoneyTheme {
                var showSplash by remember { mutableStateOf(true) }
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = BackgroundMainColor),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    if (showSplash) {
                        SplashScreen { showSplash = false }
                    } else {
                        MainScreen(viewModelFactory)
                    }
                }
            }
        }
    }

    private fun loadInitialAccount(getAccountsUseCase: GetAccountsUseCase) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = getAccountsUseCase.execute()
                if (result is ApiResult.Success && result.data.isNotEmpty()) {
                    AccountManager.selectedAccountId = result.data.first().id.toInt()
                }
            } catch (e: Exception) {
                Log.e("InitializeAccount", "Failed to init account", e)
            }
        }
    }
}