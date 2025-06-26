package com.example.data.dto.category

data class CategoryResponse(
    val id: Int,
    val name: String,
    val emoji: String,
    val isIncome: Boolean
)