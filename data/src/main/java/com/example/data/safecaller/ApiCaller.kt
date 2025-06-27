package com.example.data.safecaller

import com.example.domain.response.ApiResult
/**
 * Интерфейс для безопасного выполнения сетевых запросов с обработкой ошибок и повторными попытками.
 *
 * Основные возможности:
 * - Автоматические повторы при сбоях
 * - Настраиваемая политика повторных попыток
 * - Централизованная обработка ошибок
 *
 * @param block Выполняемый блок с API-вызовом
 * @param maxRetries Максимальное количество попыток (по умолчанию 3)
 * @param retryDelay Базовая задержка между попытками в миллисекундах (по умолчанию 2000)
 * @param shouldRetry Функция-предикат для определения необходимости повторной попытки
 * @return Результат выполнения в виде ApiResult<T>
 */
interface ApiCaller {
    suspend fun <T> safeApiCall(
        block: suspend () -> T,
        maxRetries: Int = 3,
        retryDelay: Long = 2000,
        shouldRetry: (Throwable) -> Boolean = ApiCallHelper.defaultRetryCondition
    ): ApiResult<T>
}