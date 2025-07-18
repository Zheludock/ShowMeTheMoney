package com.example.domain.model
/**
 * Доменная модель транзакции, содержащая(пока что) все необходимые данные для отображения
 * и бизнес-логики приложения.
 *
 * @property id Уникальный идентификатор транзакции в формате строки
 * @property emoji Эмодзи, ассоциированное с категорией транзакции
 * @property categoryName Название категории транзакции
 * @property amount Сумма транзакции в строковом формате.
 * @property transactionDate Дата совершения транзакции в формате строки
 * @property comment Дополнительный комментарий к транзакции (может быть null)
 * @property currency Валюта транзакции (3-буквенный код ISO). Пример: "RUB", "USD"
 * @property createdAt Дата создания записи в формате строки
 * @property updatedAt Дата последнего обновления в формате строки
 * @property isIncome Флаг, указывающий тип транзакции:
 *               - true: доход
 *               - false: расход
 *
 */
data class TransactionDomain(
    val id: Int,
    val emoji: String,
    val categoryName: String,
    val categoryId: Int,
    val amount: String,
    val transactionDate: String,
    val comment: String?,
    val currency: String,
    val isIncome: Boolean
)
