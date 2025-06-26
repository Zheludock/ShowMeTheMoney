package com.example.data

import com.example.data.dto.category.toDomain
import com.example.data.safecaller.ApiCallHelper
import com.example.domain.ApiResult
import com.example.domain.model.CategoryDomain
import com.example.domain.repository.CategoriesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoriesRepositoryImpl @Inject constructor(
    private val apiService: CategoriesApiService,
    private val apiCallHelper: ApiCallHelper
) : CategoriesRepository {

    override suspend fun getAllCategories(): ApiResult<List<CategoryDomain>> {
        return apiCallHelper.safeApiCall(block = {
            apiService.getAllCategories().map { it.toDomain() }
        })
    }

    override suspend fun getCategoriesByType(isIncome: Boolean): ApiResult<List<CategoryDomain>> {
        return apiCallHelper.safeApiCall(block = {
            apiService.getCategoriesByType(isIncome).map { it.toDomain() }
        })
    }
}