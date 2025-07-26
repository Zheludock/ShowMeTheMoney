package com.example.settings.pin

import androidx.compose.runtime.Composable

@Composable
fun PinNavigation(
    viewModel: PinViewModel,
    onAuthSuccess: () -> Unit
) {
    EnterPinScreen(
        viewModel = viewModel,
        onPinCorrect = onAuthSuccess,
        onForgotPin = {
            viewModel.pinCodeManager.clearPin()
            onAuthSuccess()
        }
    )
}