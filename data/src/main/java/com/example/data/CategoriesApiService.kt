package com.example.data

import com.example.data.dto.category.CategoryResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CategoriesApiService {

    @GET("categories")
    suspend fun getAllCategories(): List<CategoryResponse>

    @GET("categories/type/{isIncome}")
    suspend fun getCategoriesByType(
        @Path("isIncome") isIncome: Boolean
    ): List<CategoryResponse>

}