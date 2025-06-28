package com.example.data.safecaller.retrypolicy

import com.example.data.safecaller.ApiCallHelper
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * Реализация политики повторных попыток с экспоненциальной задержкой.
 *
 * @property maxRetries Максимальное количество попыток (по умолчанию 3)
 * @property retryDelay Базовая задержка в миллисекундах (по умолчанию 2000)
 * @property shouldRetry Условие для повторной попытки
 *
 * Особенности:
 * - Экспоненциальное увеличение задержки (retryDelay * номер попытки)
 * - Поддержка кастомных условий для повторных попыток
 */
class ExponentialBackoffRetryPolicy @Inject constructor(): RetryPolicy {
    private val maxRetries = 3
    private val retryDelay = 2000L
    private val shouldRetry: (Throwable) -> Boolean = ApiCallHelper.Companion.defaultRetryCondition

    override suspend fun <T> execute(block: suspend () -> T): T {
        var retryCount = 0
        var lastError: Throwable? = null

        while (retryCount < maxRetries) {
            try {
                return block()
            } catch (e: Throwable) {
                lastError = e
                if (!shouldRetry(e) || retryCount >= maxRetries - 1) {
                    throw e
                }
                delay(retryDelay * (retryCount + 1))
                retryCount++
            }
        }
        throw lastError ?: IllegalStateException("Unknown error")
    }
}