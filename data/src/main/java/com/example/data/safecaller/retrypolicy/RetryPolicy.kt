package com.example.data.safecaller.retrypolicy
/**
 * Интерфейс политики повторных попыток выполнения операций.
 */
interface RetryPolicy {
    suspend fun <T> execute(block: suspend () -> T): T
}