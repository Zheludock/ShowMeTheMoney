package com.example.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.CategoryDomain
import com.example.domain.response.ApiResult
import com.example.domain.usecase.GetAllCategoriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для управления состоянием категорий и их загрузки.
 *
 * @param getAllCategoriesUseCase UseCase для получения списка категорий.
 *
 * @property categories StateFlow с состоянием загрузки категорий (Loading, Success, Error).
 *
 * @method loadCategories Загружает категории и обновляет состояние [_categories].
 *                       Автоматически вызывается при инициализации ViewModel.
 */
class CategoryViewModel @Inject constructor(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase
) : ViewModel() {

    private val _categories = MutableStateFlow<ApiResult<List<CategoryItem>>>(ApiResult.Loading)
    val categories: StateFlow<ApiResult<List<CategoryItem>>> = _categories

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _categories.value = ApiResult.Loading
            when (val result = getAllCategoriesUseCase.execute()) {
                is ApiResult.Success -> {
                    val mappedItems = result.data.map { it.toCategoryItem() }
                    _categories.value = ApiResult.Success(mappedItems)
                }
                is ApiResult.Error -> {
                    _categories.value = result
                }
                ApiResult.Loading -> Unit
            }
        }
    }
}

fun CategoryDomain.toCategoryItem(): CategoryItem {
    return CategoryItem(
        isIncome = isIncome,
        id = categoryId,
        emoji = emoji,
        name = categoryName
    )
}