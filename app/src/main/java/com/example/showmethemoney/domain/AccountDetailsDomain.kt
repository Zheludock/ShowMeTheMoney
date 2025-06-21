package com.example.showmethemoney.domain

data class AccountDetailsDomain(
    val id: String,
    val name: String,
    val balance: String,
    val currency: String,
    val incomeStats: List<CategoryStatsDomain>,
    val expenseStats: List<CategoryStatsDomain>,
    val createdAt: String,
    val updatedAt: String
)