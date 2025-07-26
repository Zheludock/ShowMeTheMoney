package com.example.settings.pin

data class PinUiState(
    val screenState: PinScreenState = PinScreenState.Initial,
    val currentPin: String = "",
    val firstPin: String = "",
    val pinLength: Int = 4,
    val isChangingPin: Boolean = false
)