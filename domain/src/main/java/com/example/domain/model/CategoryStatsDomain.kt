package com.example.domain.model
/**
 * Доменная модель статистики по категории, содержащая агрегированные данные
 * о расходах/доходах в конкретной категории.
 *
 * @property categoryId Уникальный идентификатор категории в системе
 * @property categoryName Локализованное название категории для отображения
 * @property emoji Эмодзи, визуально представляющее категорию
 * @property amount Суммарная сумма операций в этой категории.
 */
data class CategoryStatsDomain(
    val categoryId: Int,
    val categoryName: String,
    val emoji: String,
    val amount: String
)