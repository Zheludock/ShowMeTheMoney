package com.example.showmethemoney.data.safecaller

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(
    networkMonitor: NetworkMonitor,
    block: suspend () -> T,
    maxRetries: Int = 3,
    retryDelay: Long = 2000
): ApiResult<T> = withContext(Dispatchers.IO) {
    if (!networkMonitor.isOnline()) {
        return@withContext ApiResult.Error(ApiError.NoInternetError)
    }

    var retryCount = 0
    var lastError: Exception? = null

    while (retryCount < maxRetries)
        try {
        val result = block()
        return@withContext ApiResult.Success(result)
    }
        catch (e: HttpException) {
        lastError = e
        if (e.code() == 500 && retryCount < maxRetries - 1) {
            delay(retryDelay)
            retryCount++
            continue
        }
        return@withContext ApiResult.Error(
            when (e.code()) {
                204 -> ApiError.HttpError(204, "No content")
                400 -> ApiError.HttpError(400, "Неверный формат данных")
                401 -> ApiError.HttpError(401, "Неавторизованный доступ")
                404 -> ApiError.HttpError(404, "Ресурс не найден")
                409 -> ApiError.HttpError(409, "Конфликт: у счета есть транзакции")
                500 -> ApiError.HttpError(500, "Внутренняя ошибка сервера")
                else -> ApiError.HttpError(e.code(), e.message())
            }
        )
    } catch (e: IOException) {
        lastError = e
        if (retryCount < maxRetries - 1) {
            delay(retryDelay)
            retryCount++
            continue
        }
        return@withContext ApiResult.Error(ApiError.NetworkError(e.message ?: "Сетевая ошибка"))
    } catch (e: Exception) {
        lastError = e
        return@withContext ApiResult.Error(ApiError.UnknownError(e.message ?: "Неизвестная ошибка"))
    }

    return@withContext ApiResult.Error(
        ApiError.UnknownError(lastError?.message ?: "Неизвестная ошибка после $maxRetries попыток")
    )
}