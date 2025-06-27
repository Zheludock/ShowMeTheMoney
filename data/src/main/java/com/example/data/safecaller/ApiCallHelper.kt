package com.example.data.safecaller

import com.example.data.safecaller.errorhandler.ErrorHandler
import com.example.data.safecaller.retrypolicy.RetryPolicy
import com.example.domain.response.ApiError
import com.example.domain.response.ApiResult
import com.example.domain.repository.NetworkMonitor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Реализация ApiCaller для обработки сетевых запросов.
 *
 * @property networkMonitor Монитор сетевого подключения
 * @property retryPolicy Политика повторных попыток
 * @property errorHandler Обработчик ошибок
 *
 * Особенности:
 * - Проверка интернет-соединения перед запросом
 * - Использование IO-диспетчера для сетевых операций
 * - Делегирование повторных попыток retryPolicy
 * - Делегирование обработки ошибок errorHandler
 */
@Singleton
class ApiCallHelper @Inject constructor(
    private val networkMonitor: NetworkMonitor,
    private val retryPolicy: RetryPolicy,
    private val errorHandler: ErrorHandler
) : ApiCaller {

    companion object {
        /**
         * Стандартное условие для повторной попытки запроса.
         * Повторяет для:
         * - HTTP ошибок 5xx (серверные ошибки)
         * - IO исключений (проблемы с сетью)
         */
        val defaultRetryCondition: (Throwable) -> Boolean = { error ->
            when (error) {
                is HttpException -> error.code() in 500..599
                is IOException -> true
                else -> false
            }
        }
    }
    /**
     * Безопасное выполнение API-вызова с обработкой ошибок.
     */
    override suspend fun <T> safeApiCall(
        block: suspend () -> T,
        maxRetries: Int,
        retryDelay: Long,
        shouldRetry: (Throwable) -> Boolean
    ): ApiResult<T> {
        if (!networkMonitor.isOnline.first()) {
            return ApiResult.Error(ApiError.NoInternetError)
        }

        return try {
            val result = retryPolicy.execute {
                withContext(Dispatchers.IO) { block() }
            }
            ApiResult.Success(result)
        } catch (e: Throwable) {
            errorHandler.handle(e)
        }
    }
}
