package com.example.domain.repository

import com.example.domain.response.ApiResult
import com.example.domain.model.CategoryDomain
/**
 * Репозиторий для работы с категориями транзакций.
 * Предоставляет методы для получения категорий:
 * - Всех категорий
 * - Категорий определенного типа (доход/расход)
 *
 * Все методы возвращают ApiResult с соответствующим типом данных.
 */
interface CategoriesRepository {
    /**
     * Получает полный список всех доступных категорий
     * @return ApiResult со списком CategoryDomain
     */
    suspend fun getAllCategories(): ApiResult<List<CategoryDomain>>
    /**
     * Получает категории определенного типа
     * @param isIncome true - категории доходов, false - категории расходов
     * @return ApiResult со списком CategoryDomain
     */
    suspend fun getCategoriesByType(isIncome: Boolean): ApiResult<List<CategoryDomain>>
}