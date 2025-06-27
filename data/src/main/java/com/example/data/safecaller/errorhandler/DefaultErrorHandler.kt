package com.example.data.safecaller.errorhandler

import com.example.domain.response.ApiError
import com.example.domain.response.ApiResult
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Стандартная реализация обработчика ошибок.
 * Поддерживает:
 * - HTTP ошибки (с классификацией по кодам)
 * - Ошибки сети
 * - Неизвестные ошибки
 */
class DefaultErrorHandler @Inject constructor(): ErrorHandler {
    override fun handle(error: Throwable): ApiResult.Error {
        return when (error) {
            is HttpException -> ApiResult.Error(parseHttpError(error))
            is IOException -> ApiResult.Error(ApiError.NetworkError(error.message.toString()))
            else -> ApiResult.Error(ApiError.UnknownError(error.message.toString()))
        }
    }
    /**
     * Парсинг HTTP ошибок с преобразованием в соответствующий тип ApiError.
     */
    private fun parseHttpError(error: HttpException): ApiError {
        return when (error.code()) {
            204 -> ApiError.HttpError(204, "No content")
            400 -> ApiError.HttpError(400, "Bad request")
            401 -> ApiError.HttpError(401, "Unauthorized")
            403 -> ApiError.HttpError(403, "Forbidden")
            404 -> ApiError.HttpError(404, "Not found")
            409 -> ApiError.HttpError(409, "Conflict")
            422 -> ApiError.HttpError(422, "Unprocessable entity")
            500 -> ApiError.HttpError(500, "Internal server error")
            else -> ApiError.HttpError(error.code(), error.message())
        }
    }
}