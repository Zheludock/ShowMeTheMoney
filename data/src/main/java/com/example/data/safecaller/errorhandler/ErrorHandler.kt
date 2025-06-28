package com.example.data.safecaller.errorhandler

import com.example.domain.response.ApiResult
/**
 * Интерфейс обработчика ошибок API.
 */
interface ErrorHandler {
    fun handle(error: Throwable): ApiResult.Error
}