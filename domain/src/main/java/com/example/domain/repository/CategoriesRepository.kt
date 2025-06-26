package com.example.domain.repository

import com.example.domain.ApiResult
import com.example.domain.model.CategoryDomain

interface CategoriesRepository {

    suspend fun getAllCategories(): ApiResult<List<CategoryDomain>>

    suspend fun getCategoriesByType(isIncome: Boolean): ApiResult<List<CategoryDomain>>
}