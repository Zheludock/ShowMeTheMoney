package com.example.data.dto.transaction

/**
 * DTO (Data Transfer Object) для создания новой транзакции.
 * Используется при отправке запроса на создание транзакции на сервер.
 *
 * @property accountId Идентификатор
 * @property categoryId Идентификатор категории
 * @property amount Сумма транзакции
 * @property transactionDate Дата транзакции
 * @property comment Дополнительный комментарий
 */
data class CreateTransactionRequest(
    val accountId: Int,
    val categoryId: Int,
    val amount: String,
    val transactionDate: String,
    val comment: String? = null
)