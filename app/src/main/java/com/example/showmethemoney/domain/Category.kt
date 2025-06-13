package com.example.showmethemoney.domain

data class Category(
    val categoryId: String,
    val categoryName: String,
    val emoji: String,
    val isIncome: Boolean,
)
