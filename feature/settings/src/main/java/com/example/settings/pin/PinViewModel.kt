package com.example.settings.pin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class PinViewModel @Inject constructor(
    val pinCodeManager: PinCodeManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(PinUiState())
    val uiState: StateFlow<PinUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<Unit>()
    val navigationEvent: SharedFlow<Unit> = _navigationEvent.asSharedFlow()

    init {
        checkExistingPin()
    }

    private fun checkExistingPin() {
        val savedPin = pinCodeManager.getPin()
        _uiState.update { currentState ->
            currentState.copy(
                screenState = if (savedPin != null) {
                    PinScreenState.ManageOptions
                } else {
                    PinScreenState.Initial
                }
            )
        }
    }

    fun addDigit(digit: String) {
        if (_uiState.value.currentPin.length < _uiState.value.pinLength) {
            _uiState.update { currentState ->
                currentState.copy(
                    currentPin = currentState.currentPin + digit
                )
            }

            if (_uiState.value.currentPin.length == _uiState.value.pinLength) {
                handlePinComplete()
            }
        }
    }

    fun removeDigit() {
        if (_uiState.value.currentPin.isNotEmpty()) {
            _uiState.update { currentState ->
                currentState.copy(
                    currentPin = currentState.currentPin.dropLast(1)
                )
            }
        }
    }

    private fun handlePinComplete() {
        when (val state = _uiState.value.screenState) {
            is PinScreenState.Initial -> handleInitialState()
            is PinScreenState.Confirm -> handleConfirmState()
            is PinScreenState.Verification -> handleVerificationState()
            else -> Unit
        }
    }

    private fun handleInitialState() {
        _uiState.update { currentState ->
            currentState.copy(
                firstPin = currentState.currentPin,
                currentPin = "",
                screenState = PinScreenState.Confirm
            )
        }
    }

    private fun handleConfirmState() {
        if (_uiState.value.currentPin == _uiState.value.firstPin) {
            pinCodeManager.savePin(_uiState.value.currentPin)
            navigateBack()
        } else {
            showError("PIN-коды не совпадают")
        }
    }

    private fun handleVerificationState() {
        if (checkPin(_uiState.value.currentPin)) {
            if (_uiState.value.isChangingPin) {
                _uiState.update { currentState ->
                    currentState.copy(
                        screenState = PinScreenState.Initial,
                        currentPin = "",
                        isChangingPin = false
                    )
                }
            } else {
                navigateBack()
            }
        } else {
            showError("Неверный PIN-код")
        }
    }

    fun showManageOptions() {
        _uiState.update { currentState ->
            currentState.copy(
                screenState = PinScreenState.ManageOptions,
                currentPin = ""
            )
        }
    }

    fun deletePin() {
        pinCodeManager.clearPin()
        navigateBack()
    }

    fun startChangePinProcess() {
        _uiState.update { currentState ->
            currentState.copy(
                screenState = PinScreenState.Verification,
                currentPin = "",
                isChangingPin = true
            )
        }
    }

    fun checkPin(enteredPin: String): Boolean {
        val savedPin = pinCodeManager.getPin()
        return enteredPin == savedPin
    }

    fun showError(message: String) {
        _uiState.update { currentState ->
            currentState.copy(
                screenState = PinScreenState.Error(message),
                currentPin = ""
            )
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _navigationEvent.emit(Unit)
        }
    }
}