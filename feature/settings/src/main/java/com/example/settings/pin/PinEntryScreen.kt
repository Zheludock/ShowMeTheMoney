package com.example.settings.pin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PinEntryScreen(uiState: PinUiState, viewModel: PinViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = when (uiState.screenState) {
                is PinScreenState.Initial -> "Установите ваш PIN-код"
                is PinScreenState.Confirm -> "Повторите ваш PIN-код"
                is PinScreenState.Verification -> "Введите ваш PIN-код"
                is PinScreenState.Error -> "Ошибка: ${(uiState.screenState as PinScreenState.Error).message}"
                else -> ""
            },
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        PinDotsIndicator(
            pinLength = uiState.pinLength,
            enteredLength = uiState.currentPin.length
        )

        Spacer(modifier = Modifier.height(32.dp))

        PinNumpad(
            onDigitClick = { digit -> viewModel.addDigit(digit) },
            onBackspaceClick = { viewModel.removeDigit() }
        )
    }
}