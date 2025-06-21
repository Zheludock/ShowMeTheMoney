package com.example.showmethemoney.domain.utils

import com.example.showmethemoney.domain.AccountDomain
import com.example.showmethemoney.domain.CategoryDomain
import com.example.showmethemoney.domain.TransactionDomain
import com.example.showmethemoney.ui.utils.CategoryItem
import com.example.showmethemoney.ui.utils.ExpenseItem
import com.example.showmethemoney.ui.utils.IncomeItem

fun TransactionDomain.toExpenseItem(
    categoryDomain: CategoryDomain,
    accountDomain: AccountDomain
): ExpenseItem {
    return ExpenseItem(
        id = this.id,
        categoryEmoji = categoryDomain.emoji,
        categoryName = categoryDomain.categoryName,
        comment = this.comment,
        amount = this.amount,
        accountCurrency = accountDomain.currency,
        createdAt = this.createdAt
    )
}

fun TransactionDomain.toIncomeItem(
    categoryDomain: CategoryDomain,
    accountDomain: AccountDomain
): IncomeItem {
    return IncomeItem(
        id = this.id,
        categoryName = categoryDomain.categoryName,
        comment = this.comment,
        amount = this.amount,
        accountCurrency = accountDomain.currency,
        createdAt = this.createdAt
    )
}

fun CategoryDomain.toCategoryItem(): CategoryItem {
    return CategoryItem(
        id = this.categoryId,
        emoji = this.emoji,
        name = this.categoryName,
        isIncome = this.isIncome
    )
}