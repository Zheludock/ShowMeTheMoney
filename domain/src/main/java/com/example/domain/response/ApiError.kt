package com.example.domain.response

/**
 * Представляет возможные типы ошибок, которые могут возникнуть при работе с API:
 * - `HttpError`: HTTP-ошибка с кодом статуса и сообщением.
 * - `NetworkError`: сетевая ошибка (например, таймаут соединения).
 * - `UnknownError`: неизвестная ошибка с описанием.
 * - `NoInternetError`: отсутствие интернет-соединения (без дополнительных данных).
 */
sealed class ApiError {
    data class HttpError(val code: Int, val message: String) : ApiError()
    data class NetworkError(val message: String) : ApiError()
    data class UnknownError(val message: String) : ApiError()
    object NoInternetError : ApiError()
}