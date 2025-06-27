package com.example.data.dto.account
/**
 * Упрощенное представление информации о банковском счете.
 * Используется для передачи основных данных о счете без метаданных.
 *
 * @property id Уникальный идентификатор счета (положительное число)
 * @property name Название счета (2-100 символов)
 * @property balance Текущий баланс с указанием валюты (формат: "1500.50 ₽", "-500.00 $")
 * @property currency Валюта счета (3-буквенный код ISO: RUB, USD, EUR)
 */
data class AccountInfo(
    val id: Int,
    val name: String,
    val balance: String,
    val currency: String
)