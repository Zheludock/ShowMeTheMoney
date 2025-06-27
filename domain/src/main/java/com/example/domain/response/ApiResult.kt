package com.example.domain.response

/**
 * Представляет результат выполнения API-запроса с возможными состояниями:
 * - `Loading`: данные загружаются.
 * - `Success<T>`: успешный результат с данными типа `T`.
 * - `Error`: ошибка выполнения запроса (содержит объект `ApiError`).
 *
 * @param T тип данных в случае успешного результата.
 */
sealed class ApiResult<out T> {
    object Loading : ApiResult<Nothing>()
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val error: ApiError) : ApiResult<Nothing>()
}