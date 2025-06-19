package com.example.showmethemoney.domain.utils

import com.example.showmethemoney.domain.AccountDomain
import com.example.showmethemoney.domain.CategoryDomain
import com.example.showmethemoney.domain.ExpensesDomain
import com.example.showmethemoney.domain.IncomeDomain
import com.example.showmethemoney.ui.components.CategoryItem
import com.example.showmethemoney.ui.components.ExpenseItem
import com.example.showmethemoney.ui.components.IncomeItem

fun ExpensesDomain.toExpenseItem(
    categoryDomain: CategoryDomain,
    accountDomain: AccountDomain
): ExpenseItem {
    return ExpenseItem(
        id = this.id,
        categoryEmoji = categoryDomain.emoji,
        categoryName = categoryDomain.categoryName,
        comment = this.comment,
        amount = this.amount,
        accountCurrency = accountDomain.currency
    )
}

fun IncomeDomain.toIncomeItem(
    categoryDomain: CategoryDomain,
    accountDomain: AccountDomain
): IncomeItem {
    return IncomeItem(
        id = this.id,
        categoryName = categoryDomain.categoryName,
        comment = this.comment,
        amount = this.amount,
        accountCurrency = accountDomain.currency,
    )
}

fun CategoryDomain.toCategoryItem(): CategoryItem {
    return CategoryItem(
        id = this.categoryId,
        emoji = this.emoji,
        name = this.categoryName
    )
}