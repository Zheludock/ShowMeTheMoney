package com.example.showmethemoney.ui.screens.sections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.showmethemoney.data.AccountManager
import com.example.showmethemoney.data.FinanceRepository
import com.example.showmethemoney.data.dto.category.toDomain
import com.example.showmethemoney.data.safecaller.ApiError
import com.example.showmethemoney.data.safecaller.ApiResult
import com.example.showmethemoney.data.safecaller.NetworkMonitor
import com.example.showmethemoney.data.safecaller.safeApiCall
import com.example.showmethemoney.domain.utils.toCategoryItem
import com.example.showmethemoney.ui.components.CategoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: FinanceRepository,
    private val networkMonitor: NetworkMonitor,
    private val accountManager: AccountManager
) : ViewModel() {

    private val _categories = MutableStateFlow<ApiResult<List<CategoryItem>>>(ApiResult.Loading)
    val categories: StateFlow<ApiResult<List<CategoryItem>>> = _categories

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            val accountId = accountManager.selectedAccountId
            if (accountId == -1) {
                _categories.value = ApiResult.Error(ApiError.UnknownError("Account not selected"))
                return@launch
            }

            _categories.value = ApiResult.Loading
            _categories.value = safeApiCall(
                networkMonitor = networkMonitor,
                block = {
                    repository.getCategoriesByType(false)
                        .map { category ->
                            category.toDomain().toCategoryItem()
                        }
                }
            )
        }
    }
}