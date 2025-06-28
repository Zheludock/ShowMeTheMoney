package com.example.data.di

import com.example.data.safecaller.ApiCallHelper
import com.example.data.safecaller.ApiCaller
import com.example.data.safecaller.errorhandler.DefaultErrorHandler
import com.example.data.safecaller.errorhandler.ErrorHandler
import com.example.data.safecaller.retrypolicy.ExponentialBackoffRetryPolicy
import com.example.data.safecaller.retrypolicy.RetryPolicy
import dagger.Binds
import dagger.Module
/**
 * Dagger-модуль для безопасного взаимодействия с API.
 *
 * Предоставляет зависимости, необходимые для:
 * - Обработки сетевых запросов с автоматическим повтором и обработкой ошибок.
 * - Конвертации ошибок API в пользовательские/системные исключения.
 * - Управления политикой повтора запросов при сбоях.
 *
 * ### Компоненты:
 * - [ApiCaller] → Реализация [ApiCallHelper]: обёртка для выполнения запросов с обработкой ошибок.
 * - [ErrorHandler] → Реализация [DefaultErrorHandler]: преобразует ошибки API
 *                                                  (например, HTTP 404/500) в понятные исключения.
 * - [RetryPolicy] → Реализация [ExponentialBackoffRetryPolicy]:
 *                                          политика повтора запросов с экспоненциальной задержкой.
 *
 * @see ApiCallHelper Детали обработки запросов.
 * @see DefaultErrorHandler Стандартная логика обработки ошибок.
 * @see ExponentialBackoffRetryPolicy Детали повтора запросов.
 */
@Module
abstract class SafeApiModule {

    @Binds
    abstract fun provideApiCaller(
        apiCallHelper: ApiCallHelper
    ): ApiCaller

    @Binds
    abstract fun bindErrorHandler(
        defaultErrorHandler: DefaultErrorHandler
    ): ErrorHandler

    @Binds
    abstract fun bindRetryPolicy(
        exponentialBackoffRetryPolicy: ExponentialBackoffRetryPolicy
    ): RetryPolicy
}