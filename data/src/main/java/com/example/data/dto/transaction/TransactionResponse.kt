package com.example.data.dto.transaction

import com.example.data.dto.account.AccountInfo
import com.example.data.dto.category.CategoryResponse
/**
 * DTO (Data Transfer Object) для представления транзакции в API.
 * Содержит полную информацию о финансовой транзакции.
 *
 * @property id Уникальный идентификатор транзакции
 * @property account Информация о связанном счете [AccountInfo]
 * @property category Информация о категории транзакции [CategoryResponse]
 * @property amount Сумма транзакции в строковом формате
 * @property transactionDate Дата совершения транзакции
 * @property comment Дополнительный комментарий к транзакции (может быть null)
 * @property createdAt Дата создания записи
 * @property updatedAt Дата последнего обновления
 */
data class TransactionResponse(
    val id: Int,
    val account: AccountInfo,
    val category: CategoryResponse,
    val amount: String,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String
)