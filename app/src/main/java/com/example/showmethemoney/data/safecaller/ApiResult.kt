package com.example.showmethemoney.data.safecaller

sealed class ApiResult<out T> {
    object Loading : ApiResult<Nothing>()
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val error: ApiError) : ApiResult<Nothing>()
}

