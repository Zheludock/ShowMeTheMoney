package com.example.domain.model

import java.util.Date

/**
 * Детализированная доменная модель банковского счёта, содержащая:
 * - Основную информацию о счёте
 * - Статистику по доходам и расходам
 * - Времення создания и обновления
 *
 * @property id Уникальный идентификатор счёта
 * @property name Название счёта для отображения
 * @property balance Текущий баланс
 * @property currency Валюта счёта (3-буквенный код ISO: "RUB", "USD", "EUR")
 * @property incomeStats Статистика по доходным операциям, сгруппированная по категориям.
 * @property expenseStats Статистика по расходным операциям, сгруппированная по категориям.
 * @property createdAt Дата создания счёта
 * @property updatedAt Дата последнего обновления
 */
data class AccountDetailsDomain(
    val id: Int,
    val name: String,
    val balance: String,
    val currency: String,
    val incomeStats: List<CategoryStatsDomain>,
    val expenseStats: List<CategoryStatsDomain>,
    val createdAt: Date,
    val updatedAt: Date?
)