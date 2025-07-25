package com.example.showmethemoney.ui.utils

import android.content.Context
import android.util.Log
import com.example.category.toCategoryItem
import com.example.domain.response.ApiResult
import com.example.domain.usecase.account.GetAccountDetailsUseCase
import com.example.domain.usecase.account.GetAccountsUseCase
import com.example.domain.usecase.category.GetAllCategoriesUseCase
import com.example.utils.AccountManager
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * Сервис инициализации аккаунта пользователя при старте приложения.
 *
 * ## Основная ответственность:
 * - Загрузка списка аккаунтов через [GetAccountsUseCase]
 * - Установка первого доступного аккаунта как выбранного в [com.example.utils.AccountManager]
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
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
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
            val category = getAllCategoriesUseCase.execute()

            val savedAccountId = sharedPreferences.getString("account_id", null)?.toInt()

            if (savedAccountId != null) {
                val detailsResult = getAccountDetailsUseCase.execute(savedAccountId)
                AccountManager.selectedAccountId = detailsResult.id
                AccountManager.updateAcc(detailsResult.currency, detailsResult.name)
                Log.d("AccountInitializer", "Account details loaded from backend")
            }

            Log.d("AccountInitializer", "No saved account ID, fetching accounts list")

            val accountsResult = getAccountsUseCase.execute()
            if (accountsResult != null) {
                val account = accountsResult.first()

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