package com.example.showmethemoney.domain

data class CategoryDomain(
    val categoryId: String,
    val categoryName: String,
    val emoji: String,
    val isIncome: Boolean,
)
