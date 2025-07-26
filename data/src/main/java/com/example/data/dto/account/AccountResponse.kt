package com.example.data.dto.account

import java.util.Date

/**
 * DTO (Data Transfer Object) для представления банковского счета в API.
 * Содержит полную информацию о банковском счете пользователя.
 *
 * @property id Уникальный идентификатор счета
 * @property userId Идентификатор владельца счета
 * @property name Название счета (2-100 символов)
 * @property balance Текущий баланс с указанием валюты (формат: "1500.50 ₽")
 * @property currency Валюта счета (3-буквенный код ISO: "RUB", "USD", "EUR")
 * @property createdAt Дата создания в ISO-формате ("yyyy-MM-dd'T'HH:mm:ss")
 * @property updatedAt Дата последнего обновления в ISO-формате
 */
data class AccountResponse(
    val id: Int,
    val userId: Int,
    val name: String,
    val balance: String,
    val currency: String,
    val createdAt: Date,
    val updatedAt: Date?
)
