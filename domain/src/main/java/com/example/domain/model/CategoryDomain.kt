package com.example.domain.model
/**
 * Доменная модель категории транзакций.
 * Содержит основные данные для идентификации и отображения категории в UI.
 *
 * @property categoryId Уникальный идентификатор категории.
 * @property categoryName Название категории для отображения пользователю.
 * @property emoji Визуальное представление категории в виде эмодзи.
 * @property isIncome Флаг, определяющий тип категории:
 *                   - true: категория доходов (зарплата, подарки)
 *                   - false: категория расходов (еда, транспорт)
 */
data class CategoryDomain(
    val categoryId: Int,
    val categoryName: String,
    val emoji: String,
    val isIncome: Boolean,
)
