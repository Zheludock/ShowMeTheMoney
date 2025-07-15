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
    override suspend fun getAllCategories(): ApiResult<List<CategoryDomain>> {
        return apiCallHelper.safeApiCall(block = {
            val cached = categoryDao.getAllCategories()
            if (cached.isNotEmpty()) {
                cached.map { it.toDomain() }
            } else {
                val remote = apiService.getAllCategories()
                categoryDao.insertCategories(remote.map { it.toEntity() })
                remote.map { it.toDomain() }
            }
        })
    }

    override suspend fun getCategoriesByType(isIncome: Boolean): ApiResult<List<CategoryDomain>> {
        return apiCallHelper.safeApiCall(block = {
            val cached = categoryDao.getCategoriesByType(isIncome)
            if (cached.isNotEmpty()) {
                cached.map { it.toDomain() }
            } else {
                val remote = apiService.getCategoriesByType(isIncome)
                categoryDao.insertCategories(remote.map { it.toEntity() })
                remote.map { it.toDomain() }
            }
        })
    }
}