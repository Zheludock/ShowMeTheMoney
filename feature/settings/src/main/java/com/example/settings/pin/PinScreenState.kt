package com.example.settings.pin

sealed class PinScreenState {
    object Initial : PinScreenState()
    object Confirm : PinScreenState()
    object Verification : PinScreenState()
    data class Error(val message: String) : PinScreenState()
    object ManageOptions : PinScreenState()
}