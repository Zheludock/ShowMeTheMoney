package com.example.data.safecaller

import com.example.domain.ApiError
import com.example.domain.ApiResult
import com.example.domain.repository.NetworkMonitor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiCallHelper @Inject constructor(
    private val networkMonitor: NetworkMonitor
) {
    suspend fun <T> safeApiCall(
        block: suspend () -> T,
        maxRetries: Int = 3,
        retryDelay: Long = 2000,
        shouldRetry: (Throwable) -> Boolean = ::defaultRetryCondition
    ): ApiResult<T> {
        if (!networkMonitor.isOnline.first()) {
            return ApiResult.Error(ApiError.NoInternetError)
        }

        var retryCount = 0
        var lastError: Throwable? = null

        while (retryCount < maxRetries) {
            try {
                val result = withContext(Dispatchers.IO) {
                    block()
                }
                return ApiResult.Success(result)
            } catch (e: Throwable) {
                lastError = e
                when {
                    !shouldRetry(e) -> return handleSpecificError(e)
                    retryCount < maxRetries - 1 -> {
                        delay(retryDelay)
                        retryCount++
                    }
                    else -> return handleSpecificError(e)
                }
            }
        }

        return ApiResult.Error(
            ApiError.UnknownError(lastError?.message ?: "Unknown error after $maxRetries retries")
        )
    }

    private fun handleSpecificError(error: Throwable): ApiResult.Error {
        return when (error) {
            is HttpException -> {
                val apiError = when (error.code()) {
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
                ApiResult.Error(apiError)
            }
            is IOException -> ApiResult.Error(ApiError.NetworkError("Network error: ${error.message}"))
            else -> ApiResult.Error(ApiError.UnknownError(error.message ?: "Unknown error"))
        }
    }

    private fun defaultRetryCondition(error: Throwable): Boolean {
        return when (error) {
            is HttpException -> error.code() in 500..599
            is IOException -> true
            else -> false
        }
    }
}