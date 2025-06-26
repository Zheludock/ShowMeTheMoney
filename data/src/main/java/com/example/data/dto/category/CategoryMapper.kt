package com.example.data.dto.category

import com.example.domain.model.CategoryDomain
import com.example.domain.model.CategoryStatsDomain

fun CategoryResponse.toDomain(): CategoryDomain {
    return CategoryDomain(
        categoryId = id.toString(),
        categoryName = name,
        emoji = emoji,
        isIncome = isIncome
    )
}

fun CategoryStats.toDomain(): CategoryStatsDomain {
    return CategoryStatsDomain(
        categoryId = categoryId.toString(),
        categoryName = categoryName,
        emoji = emoji,
        amount = amount
    )
}