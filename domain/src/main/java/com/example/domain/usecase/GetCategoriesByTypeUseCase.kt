package com.example.domain.usecase

import com.example.domain.response.ApiResult
import com.example.domain.model.CategoryDomain
import com.example.domain.repository.CategoriesRepository
import javax.inject.Inject
/**
 * UseCase для получения списка категорий по типу (доход/расход).
 *
 * @param repository репозиторий для работы с категориями
 *
 * @param isIncome флаг, определяющий тип категорий:
 *                 true - категории доходов,
 *                 false - категории расходов
 *
 * @return ApiResult со списком CategoryDomain в случае успеха
 *         или ошибкой ApiError в случае неудачи
 */
class GetCategoriesByTypeUseCase @Inject constructor(
    private val repository: CategoriesRepository
) {
    suspend fun execute(
        isIncome: Boolean
    ): ApiResult<List<CategoryDomain>> {
        return repository.getCategoriesByType(isIncome)
    }
}