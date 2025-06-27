package com.example.domain.usecase

import com.example.domain.response.ApiResult
import com.example.domain.model.CategoryDomain
import com.example.domain.repository.CategoriesRepository
import javax.inject.Inject
/**
 * UseCase для получения полного списка всех категорий.
 *
 * @param repository репозиторий для работы с категориями
 *
 * @return ApiResult со списком всех CategoryDomain в случае успеха
 *         или ошибкой ApiError в случае неудачи
 */
class GetAllCategoriesUseCase @Inject constructor(
        private val repository: CategoriesRepository) {
    suspend fun execute(): ApiResult<List<CategoryDomain>> {
        return repository.getAllCategories()
    }
}