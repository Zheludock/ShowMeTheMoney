package com.example.settings.pin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun EnterPinScreen(
    viewModel: PinViewModel,
    onPinCorrect: () -> Unit,
    onForgotPin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect {
            onPinCorrect()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (val state = uiState.screenState) {
            is PinScreenState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            else -> Unit
        }

        Text(
            text = "Введите ваш PIN-код",
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

        Spacer(modifier = Modifier.height(24.dp))

        TextButton(
            onClick = onForgotPin,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(
                text = "Забыли PIN-код?",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

    LaunchedEffect(uiState.currentPin) {
        if (uiState.currentPin.length == uiState.pinLength) {
            if (viewModel.checkPin(uiState.currentPin)) {
                onPinCorrect()
            } else {
                viewModel.showError("Неверный PIN-код")
            }
        }
    }
}