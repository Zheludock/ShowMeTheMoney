package com.example.data.dto.account

import com.example.data.dto.category.CategoryStats
import java.util.Date

/**
 * Детализированный ответ API по аккаунту.
 * @property id Идентификатор аккаунта.
 * @property name Название аккаунта.
 * @property balance Текущий баланс.
 * @property currency Валюта.
 * @property incomeStats Статистика по доходам (по категориям).
 * @property expenseStats Статистика по расходам (по категориям).
 * @property createdAt Время создания аккаунта.
 * @property updatedAt Время последнего обновления.
 */
data class AccountDetailsResponse(
    val id: Int,
    val name: String,
    val balance: String,
    val currency: String,
    val incomeStats: List<CategoryStats>,
    val expenseStats: List<CategoryStats>,
    val createdAt: Date,
    val updatedAt: Date?
)