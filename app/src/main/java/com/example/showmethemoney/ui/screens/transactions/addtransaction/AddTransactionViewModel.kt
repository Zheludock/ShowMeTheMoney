/*
package com.example.showmethemoney.ui.screens.transactions.addtransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.response.ApiResult
import com.example.domain.usecase.GetCategoriesByTypeUseCase
import com.example.showmethemoney.ui.screens.category.CategoryItem
import com.example.showmethemoney.ui.utils.toCategoryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject
*/
/**
 * Не оценивать, в процессе. В ТЗ пока не фигурирует
 *//*

class AddTransactionViewModel @Inject constructor(
    private val getCategoriesByTypeUseCase: GetCategoriesByTypeUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(TransactionUiState())
    val uiState: StateFlow<TransactionUiState> = _uiState.asStateFlow()

    private val _categories = MutableStateFlow<ApiResult<List<CategoryItem>>>(ApiResult.Loading)
    val categories: StateFlow<ApiResult<List<CategoryItem>>> = _categories

    private val _showCategoryDialog = MutableStateFlow(false)
    val showCategoryDialog: StateFlow<Boolean> = _showCategoryDialog.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _categories.value = ApiResult.Loading
            when (val result = getCategoriesByTypeUseCase.execute(isIncome)) {
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

    fun showCategoryDialog(show: Boolean) {
        _showCategoryDialog.value = show
    }

    fun updateSelectedCategory(category: CategoryItem) {
        _uiState.update { it.copy(
            selectedCategoryId = category.id,
            categoryName = category.name
        ) }
        showCategoryDialog(false)
    }

    fun createTransaction(
        accountId: String = "67",
        categoryId: String,
        amount: String,
        transactionDate: String,
        comment: String? = null
    ) {
        viewModelScope.launch {
            _createTransactionResult.value =
        }
    }

    fun updateAmount(amount: String) {
        _uiState.update { it.copy(amount = amount) }
    }

    fun updateTransactionDate(date: Date) {
        _uiState.update { it.copy(transactionDate = date) }
    }

    fun getBackendFormattedDate(): String {
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(_uiState.value.transactionDate)
    }

    fun updateComment(comment: String) {
        _uiState.update { it.copy(comment = comment) }
    }
}
*/
