package com.example.showmethemoney.domain.utils

import com.example.showmethemoney.domain.Account
import com.example.showmethemoney.domain.Category
import com.example.showmethemoney.domain.Expenses
import com.example.showmethemoney.domain.Income
import com.example.showmethemoney.ui.components.CategoryItem
import com.example.showmethemoney.ui.components.ExpenseItem
import com.example.showmethemoney.ui.components.IncomeItem

fun Expenses.toExpenseItem(
    category: Category,
    account: Account
): ExpenseItem {
    return ExpenseItem(
        id = this.id,
        articleEmoji = category.emoji,
        articleName = category.categoryName,
        comment = this.comment,
        amount = this.amount,
        accountCurrency = account.currency
    )
}

fun Income.toIncomeItem(
    category: Category,
    account: Account
): IncomeItem {
    return IncomeItem(
        id = this.id,
        articleName = category.categoryName,
        comment = this.comment,
        amount = this.amount,
        accountCurrency = account.currency,
    )
}

fun Category.toCategoryItem(): CategoryItem {
    return CategoryItem(
        id = this.categoryId,
        emoji = this.emoji,
        name = this.categoryName
    )
}