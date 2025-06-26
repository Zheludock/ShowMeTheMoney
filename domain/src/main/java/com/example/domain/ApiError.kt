package com.example.domain

sealed class ApiError {
    data class HttpError(val code: Int, val message: String) : ApiError()
    data class NetworkError(val message: String) : ApiError()
    data class UnknownError(val message: String) : ApiError()
    object NoInternetError : ApiError()
}