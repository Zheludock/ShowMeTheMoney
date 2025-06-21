package com.example.showmethemoney.domain

data class CategoryStatsDomain(
    val categoryId: String,
    val categoryName: String,
    val emoji: String,
    val amount: String
)