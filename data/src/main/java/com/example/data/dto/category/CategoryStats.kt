package com.example.data.dto.category
/**
 * DTO (Data Transfer Object) для статистики по категориям.
 * Содержит агрегированные данные о расходах/доходах по конкретной категории.
 *
 * @property categoryId Уникальный идентификатор категории
 * @property categoryName Название категории для отображения
 * @property emoji Эмодзи категории
 * @property amount Сумма операций по категории
 */
data class CategoryStats(
    val categoryId: Int,
    val categoryName: String,
    val emoji: String,
    val amount: String
)
