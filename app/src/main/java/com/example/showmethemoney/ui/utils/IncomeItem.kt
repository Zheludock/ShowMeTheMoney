package com.example.showmethemoney.ui.utils

data class IncomeItem(
    val id: String,
    val categoryName: String,
    val comment: String?,
    val amount: String,
    val accountCurrency: String,
    val createdAt: String? = null
)

