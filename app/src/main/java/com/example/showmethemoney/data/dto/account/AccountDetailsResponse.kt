package com.example.showmethemoney.data.dto.account

import com.example.showmethemoney.data.dto.category.CategoryStats

data class AccountDetailsResponse(
    val id: Int,
    val name: String,
    val balance: String,
    val currency: String,
    val incomeStats: List<CategoryStats>,
    val expenseStats: List<CategoryStats>,
    val createdAt: String,
    val updatedAt: String
)