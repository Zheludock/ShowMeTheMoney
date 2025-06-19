package com.example.showmethemoney.ui.components

data class ExpenseItem(
    val id: String,
    val categoryEmoji: String,
    val categoryName: String,
    val comment: String?,
    val amount: String,
    val accountCurrency: String
)