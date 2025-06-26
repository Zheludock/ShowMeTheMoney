package com.example.showmethemoney.ui.utils
/**
 * Модель категории для доходов/расходов.
 *
 * @property id Уникальный идентификатор категории.
 * @property emoji Emoji-иконка категории.
 * @property name Название категории.
 * @property isIncome Флаг, указывающий тип категории (true — доход, false — расход).
 */
data class CategoryItem(
    val id: String,
    val emoji: String,
    val name: String,
    val isIncome: Boolean
)