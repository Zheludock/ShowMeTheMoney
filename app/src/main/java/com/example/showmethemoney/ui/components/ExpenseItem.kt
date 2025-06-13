package com.example.showmethemoney.ui.components

data class ExpenseItem(
    val id: String,
    val articleEmoji: String,
    val articleName: String,
    val comment: String?,
    val amount: String,
    val accountCurrency: String
)