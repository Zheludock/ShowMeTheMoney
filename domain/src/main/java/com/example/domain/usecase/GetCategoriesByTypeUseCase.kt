package com.example.domain.usecase

import com.example.domain.ApiResult
import com.example.domain.model.CategoryDomain
import com.example.domain.repository.CategoriesRepository
import javax.inject.Inject

class GetCategoriesByTypeUseCase @Inject constructor(
    private val repository: CategoriesRepository
) {
    suspend fun execute(
        isIncome: Boolean
    ): ApiResult<List<CategoryDomain>> {
        return repository.getCategoriesByType(isIncome)
    }
}