package com.example.showmethemoney.data.dto.category

import com.example.showmethemoney.domain.CategoryDomain
import com.example.showmethemoney.domain.CategoryStatsDomain

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