package com.example.data.dto.category

import com.example.domain.model.CategoryDomain
import com.example.domain.model.CategoryStatsDomain
/**
 * Преобразует DTO категории из API в доменную модель.
 *
 * @receiver [CategoryResponse] DTO категории с сервера
 * @return [CategoryDomain] Доменная модель категории
 */
fun CategoryResponse.toDomain(): CategoryDomain {
    return CategoryDomain(
        categoryId = id.toString(),
        categoryName = name,
        emoji = emoji,
        isIncome = isIncome
    )
}
/**
 * Преобразует статистику по категориям из API в доменную модель.
 *
 * @receiver [CategoryStats] DTO статистики с сервера
 * @return [CategoryStatsDomain] Доменная модель статистики
 */
fun CategoryStats.toDomain(): CategoryStatsDomain {
    return CategoryStatsDomain(
        categoryId = categoryId.toString(),
        categoryName = categoryName,
        emoji = emoji,
        amount = amount
    )
}