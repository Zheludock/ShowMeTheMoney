package com.example.data.repository

import com.example.data.retrofit.CategoriesApiService
import com.example.data.dto.category.toDomain
import com.example.data.dto.category.toEntity
import com.example.data.room.dao.CategoryDao
import com.example.data.safecaller.ApiCallHelper
import com.example.domain.response.ApiResult
import com.example.domain.model.CategoryDomain
import com.example.domain.repository.CategoriesRepository
import javax.inject.Inject
import javax.inject.Singleton
/**
 * Реализация [CategoriesRepository] для работы с категориями через сетевой API.
 * Обрабатывает все операции с транзакциями и преобразует данные между доменной моделью и API.
 *
 * @param apiService Сетевой API для работы с категориями
 * @param apiCallHelper Инструмент для безопасных вызовов API с обработкой ошибок. Все обращения
 *                                                  к сети выполнять только внутри apiCallHelper!
 */
@Singleton
class CategoriesRepositoryImpl @Inject constructor(
    private val apiService: CategoriesApiService,
    private val apiCallHelper: ApiCallHelper,
    private val categoryDao: CategoryDao
) : CategoriesRepository {
    /**
     * Получает все доступные категории.
     * @return [ApiResult] с списком [CategoryDomain] или ошибкой
     */
    override suspend fun getAllCategories(): List<CategoryDomain> {
        val cached = categoryDao.getAllCategories()
        if (cached.isNotEmpty()) {
            return cached.map { it.toDomain() }
        }

        val apiResult = apiCallHelper.safeApiCall({ apiService.getAllCategories() })
        return when (apiResult) {
            is ApiResult.Success -> {
                categoryDao.insertCategories(apiResult.data.map { it.toEntity() })
                apiResult.data.map { it.toDomain() }
            }
            else -> {
                emptyList()
            }
        }
    }

    override suspend fun getCategoriesByType(isIncome: Boolean): List<CategoryDomain> {
        val cached = categoryDao.getCategoriesByType(isIncome)
        if (cached.isNotEmpty()) {
            return cached.map { it.toDomain() }
        }

        val apiResult = apiCallHelper.safeApiCall({ apiService.getAllCategories() })
        return when (apiResult) {
            is ApiResult.Success -> {
                categoryDao.insertCategories(apiResult.data.map { it.toEntity() })
                categoryDao.getCategoriesByType(isIncome).map { it.toDomain() }
            }
            else -> {
                emptyList()
            }
        }
    }
}