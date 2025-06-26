package com.example.domain.usecase

import com.example.domain.ApiResult
import com.example.domain.model.CategoryDomain
import com.example.domain.repository.CategoriesRepository
import javax.inject.Inject

class GetAllCategoriesUseCase @Inject constructor(
        private val repository: CategoriesRepository
    ) {
        suspend fun execute(): ApiResult<List<CategoryDomain>> {
            return repository.getAllCategories()
        }
    }