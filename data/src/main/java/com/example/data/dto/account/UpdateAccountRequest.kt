package com.example.data.dto.account
/**
 * DTO (Data Transfer Object) для обновления данных банковского счета.
 * Все поля опциональны (nullable), что позволяет обновлять только указанные атрибуты.
 *
 * @property name Новое название счета (опционально)
 * @property balance Новый баланс счета в строковом формате (опционально, пример: "1000.50")
 * @property currency Новая валюта счета (3-буквенный код ISO, опционально, пример: "RUB")
 */
data class UpdateAccountRequest(
    val name: String?,
    val balance: String?,
    val currency: String?
)