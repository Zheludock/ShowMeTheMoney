package com.example.data.dto.category
/**
 * DTO (Data Transfer Object) для представления категории транзакций.
 * Используется для передачи данных о категории между клиентом и сервером.
 *
 * @property id Уникальный идентификатор категории
 * @property name Название категории
 * @property emoji Эмодзи, ассоциированное с категорией
 * @property isIncome Флаг, указывающий тип категории:
 *                   - true: категория доходов
 *                   - false: категория расходов
 */
data class CategoryResponse(
    val id: Int,
    val name: String,
    val emoji: String,
    val isIncome: Boolean
)