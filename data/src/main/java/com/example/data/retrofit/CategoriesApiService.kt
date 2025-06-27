package com.example.data.retrofit

import com.example.data.dto.category.CategoryResponse
import retrofit2.http.GET
import retrofit2.http.Path
/**
 * Retrofit-интерфейс для работы с API транзакций.
 * Определяет все доступные сетевые запросы для работы с категориями.
 */
interface CategoriesApiService {
    /**
    * Получает все доступные категории транзакций.
    *
    * @return Список всех категорий в формате [CategoryResponse]
    * */
    @GET("categories")
    suspend fun getAllCategories(): List<CategoryResponse>
    /**
     * Получает категории определенного типа (доходы/расходы).
     *
     * @param isIncome Тип категорий:
     *                 - true: категории доходов
     *                 - false: категории расходов
     * @return Отфильтрованный список категорий [CategoryResponse]
     */
    @GET("categories/type/{isIncome}")
    suspend fun getCategoriesByType(
        @Path("isIncome") isIncome: Boolean
    ): List<CategoryResponse>
}