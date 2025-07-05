package com.example.showmethemoney.ui.utils

import android.content.Context
import android.util.Log
import com.example.domain.response.ApiResult
import com.example.domain.usecase.GetAccountsUseCase
import kotlinx.coroutines.delay
import javax.inject.Inject
/**
 * Сервис инициализации аккаунта пользователя при старте приложения.
 *
 * ## Основная ответственность:
 * - Загрузка списка аккаунтов через [GetAccountsUseCase]
 * - Установка первого доступного аккаунта как выбранного в [AccountManager]
 * - Обработка и логирование ошибок инициализации
 *
 * ## Пример использования:
 * ```kotlin
 * class MainActivity : ComponentActivity() {
 *     @Inject lateinit var accountInitializer: AccountInitializer
 *
 *     private fun loadInitialAccount() {
 *         lifecycleScope.launch {
 *             accountInitializer.initialize()
 *         }
 *     }
 * }
 * @property getAccountsUseCase UseCase для получения аккаунтов (внедряется через DI)
 * @see GetAccountsUseCase.execute
 * @see AccountManager.selectedAccountId
 */
class AccountInitializer @Inject constructor(
    private val getAccountsUseCase: GetAccountsUseCase,
    private val context: Context
) {
    private val sharedPreferences by lazy {
        context.getSharedPreferences("AccountPrefs", Context.MODE_PRIVATE)
    }

    /**
     * Основной метод инициализации аккаунта.
     * @throws Exception Логирует ошибки.
     */
    suspend fun initialize() {
        try {
            val savedAccountId = sharedPreferences.getString("account_id", null)?.toInt()
            val savedAccountName = sharedPreferences.getString("account_name", null)
            val savedAccountCurrency = sharedPreferences.getString("account_currency", null)

            if (savedAccountId != null && savedAccountName != null && savedAccountCurrency != null) {
                AccountManager.selectedAccountId = savedAccountId
                AccountManager.updateAcc(savedAccountCurrency, savedAccountName)
                Log.d("Shared preference is OK","used shared preference")
                return
            }
            Log.d("No shared preference", "init shared preference")

            val result = getAccountsUseCase.execute()
            if (result is ApiResult.Success && result.data.isNotEmpty()) {
                val account = result.data.first()

                with(sharedPreferences.edit()) {
                    putString("account_id", account.id.toString())
                    putString("account_name", account.name)
                    putString("account_currency", account.currency)
                    apply()
                }

                AccountManager.selectedAccountId = account.id
                AccountManager.updateAcc(account.currency, account.name)
            } else {
                delay(1000)
                initialize()
            }
        } catch (e: Exception) {
            Log.e("AccountInitializer", "Failed to init account", e)
            delay(1000)
            initialize()
        }
    }
}