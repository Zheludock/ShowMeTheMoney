package com.example.showmethemoney.ui.utils

import android.content.Context
import android.util.Log
import com.example.domain.response.ApiResult
import com.example.domain.usecase.GetAccountDetailsUseCase
import com.example.domain.usecase.GetAccountsUseCase
import com.example.ui.AccountManager
import kotlinx.coroutines.delay
import javax.inject.Inject
/**
 * Сервис инициализации аккаунта пользователя при старте приложения.
 *
 * ## Основная ответственность:
 * - Загрузка списка аккаунтов через [GetAccountsUseCase]
 * - Установка первого доступного аккаунта как выбранного в [com.example.ui.AccountManager]
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
    private val getAccountDetailsUseCase: GetAccountDetailsUseCase,
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

            if (savedAccountId != null) {
                val detailsResult = getAccountDetailsUseCase.execute(savedAccountId)
                if (detailsResult is ApiResult.Success) {
                    val account = detailsResult.data
                    AccountManager.selectedAccountId = account.id
                    AccountManager.updateAcc(account.currency, account.name)
                    Log.d("AccountInitializer", "Account details loaded from backend")
                    return
                }
                sharedPreferences.edit().remove("account_id").apply()
                Log.d("AccountInitializer", "Failed to get account details, clearing saved ID")
            }

            Log.d("AccountInitializer", "No saved account ID, fetching accounts list")

            val accountsResult = getAccountsUseCase.execute()
            if (accountsResult is ApiResult.Success && accountsResult.data.isNotEmpty()) {
                val account = accountsResult.data.first()

                with(sharedPreferences.edit()) {
                    putString("account_id", account.id.toString())
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