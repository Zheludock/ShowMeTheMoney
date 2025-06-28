package com.example.showmethemoney.ui.utils

import com.example.domain.model.CategoryDomain
import com.example.domain.model.TransactionDomain
import com.example.showmethemoney.ui.screens.category.CategoryItem
import com.example.showmethemoney.ui.screens.transactions.TransactionItem
/**
 * Преобразует объект доменного слоя TransactionDomain в объект представления TransactionItem.
 *
 * @return TransactionItem - объект для отображения, содержащий:
 *         id - идентификатор транзакции,
 *         categoryEmoji - эмодзи категории,
 *         categoryName - название категории,
 *         comment - комментарий к транзакции,
 *         amount - сумма транзакции,
 *         accountCurrency - валюта счета,
 *         isIncome - флаг дохода (true - доход, false - расход)
 */
fun TransactionDomain.toTransactionItem(): TransactionItem {
    return TransactionItem(
        id = id,
        categoryEmoji = emoji,
        categoryName = categoryName,
        comment = comment,
        amount = amount,
        accountCurrency = currency,
        createdAt = createdAt,
        isIncome = isIncome
    )
}
/**
 * Преобразует объект доменного слоя CategoryDomain в объект представления CategoryItem.
 *
 * @return CategoryItem - объект категории для отображения, содержащий:
 *         isIncome - флаг категории дохода (true - доход, false - расход),
 *         id - идентификатор категории,
 *         emoji - эмодзи категории,
 *         name - название категории
 */
fun CategoryDomain.toCategoryItem(): CategoryItem {
    return CategoryItem(
        isIncome = isIncome,
        id = categoryId,
        emoji = emoji,
        name = categoryName
    )
}
