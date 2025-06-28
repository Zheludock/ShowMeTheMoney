package com.example.showmethemoney.ui.utils

import android.util.Log
import com.example.domain.response.ApiResult
import com.example.domain.usecase.GetAccountsUseCase
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
    private val getAccountsUseCase: GetAccountsUseCase
) {
    /**
     * Основной метод инициализации аккаунта.
     * @throws Exception Логирует ошибки.
     */
    suspend fun initialize() {
        try {
            val result = getAccountsUseCase.execute()
            if (result is ApiResult.Success && result.data.isNotEmpty()) {
                AccountManager.selectedAccountId = result.data.first().id.toInt()
            }
        } catch (e: Exception) {
            Log.e("AccountInitializer", "Failed to init account", e)
        }
    }
}