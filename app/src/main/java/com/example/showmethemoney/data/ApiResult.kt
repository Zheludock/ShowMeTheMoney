package com.example.showmethemoney.data

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

sealed class ApiError {
    data class HttpError(val code: Int, val message: String) : ApiError()
    data class NetworkError(val message: String) : ApiError()
    data class UnknownError(val message: String) : ApiError()
    object NoInternetError : ApiError()
}

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val error: ApiError) : ApiResult<Nothing>()
}

interface NetworkMonitor {
    fun isOnline(): Boolean
}

class AndroidNetworkMonitor(
    private val context: Context
) : NetworkMonitor {
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override fun isOnline(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}

suspend fun <T> safeApiCall(
    networkMonitor: NetworkMonitor,
    block: suspend () -> T,
    maxRetries: Int = 3,
    retryDelay: Long = 2000
): ApiResult<T> {
    if (!networkMonitor.isOnline()) {
        return ApiResult.Error(ApiError.NoInternetError)
    }

    var retryCount = 0
    var lastError: Exception? = null

    while (retryCount < maxRetries) {
        try {
            val result = block()
            return ApiResult.Success(result)
        } catch (e: HttpException) {
            lastError = e
            if (e.code() == 500 && retryCount < maxRetries - 1) {
                delay(retryDelay)
                retryCount++
                continue
            }

            return ApiResult.Error(
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
            return ApiResult.Error(ApiError.NetworkError(e.message ?: "Сетевая ошибка"))
        } catch (e: Exception) {
            lastError = e
            return ApiResult.Error(ApiError.UnknownError(e.message ?: "Неизвестная ошибка"))
        }
    }

    return ApiResult.Error(
        ApiError.UnknownError(lastError?.message ?: "Неизвестная ошибка после $maxRetries попыток")
    )
}