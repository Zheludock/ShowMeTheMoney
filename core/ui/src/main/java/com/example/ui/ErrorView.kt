package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.response.ApiError

/**
 * Компонент для отображения ошибки API с возможностью повтора действия.
 *
 * @param error Ошибка API для отображения (должен быть sealed class/interface)
 * @param onRetry Колбэк при нажатии на кнопку повтора
 */
@Composable
fun ErrorView(error: ApiError, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Snackbar(
            action = {
                TextButton(
                    onClick = onRetry
                ) {
                    Text("Повторить")
                }
            },
            modifier = Modifier.padding(16.dp).background(MaterialTheme.colorScheme.secondary)
        ) {
            Text(
                text = when (error) {
                    is ApiError.NoInternetError -> "Нет интернет-соединения"
                    is ApiError.HttpError -> "Ошибка сервера: ${error.code}"
                    is ApiError.NetworkError -> "Сетевая ошибка"
                    is ApiError.UnknownError -> "Неизвестная ошибка"
                }
            )
        }
    }
}