package com.example.domain.model
/**
 * Модель данных для обновления транзакции.
 * Содержит все необходимые поля для изменения существующей транзакции.
 *
 * @property transactionId Уникальный идентификатор транзакции (обязательное поле)
 * @property accountId Идентификатор аккаунта, к которому относится транзакция (обязательное поле)
 * @property categoryId Идентификатор категории транзакции (обязательное поле)
 * @property amount Сумма транзакции в строковом формате (обязательное поле).
 * @property transactionDate Дата транзакции в формате строки (обязательное поле).
 * @property comment Дополнительный комментарий к транзакции (опциональное поле).
 *
 */
data class TransactionInput(
    val transactionId: Int,
    val accountId: Int,
    val categoryId: Int,
    val amount: String,
    val transactionDate: String,
    val comment: String? = null
)