package com.example.ui

import androidx.compose.runtime.mutableStateOf

/**
 * Объект-менеджер для управления данными текущего выбранного аккаунта.
 *
 * Содержит:
 * - Идентификатор выбранного аккаунта (selectedAccountId)
 * - Название аккаунта (selectedAccountName)
 * - Валюту аккаунта (selectedAccountCurrency)
 *
 * Позволяет обновлять данные аккаунта через метод updateAcc().
 */
object AccountManager {
    var selectedAccountId: Int = -1

    private val _selectedAccountName = mutableStateOf("")
    val selectedAccountName = _selectedAccountName
    private val _selectedAccountCurrency = mutableStateOf("")
    val selectedAccountCurrency = _selectedAccountCurrency

    fun updateAcc(newCurrency: String, newName: String) {
        _selectedAccountCurrency.value = newCurrency
        _selectedAccountName.value = newName
    }
}