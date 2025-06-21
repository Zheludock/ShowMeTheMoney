package com.example.showmethemoney.ui.screens.sections.subsections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.showmethemoney.data.AccountManager
import com.example.showmethemoney.data.FinanceRepository
import com.example.showmethemoney.data.dto.category.toDomain
import com.example.showmethemoney.data.dto.transaction.TransactionResponse
import com.example.showmethemoney.data.safecaller.ApiCallHelper
import com.example.showmethemoney.data.safecaller.ApiResult
import com.example.showmethemoney.domain.utils.toCategoryItem
import com.example.showmethemoney.ui.utils.CategoryItem
import com.example.showmethemoney.ui.utils.TransactionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
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

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val repository: FinanceRepository,
    private val apiCallHelper: ApiCallHelper,
    private val accountManager: AccountManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(TransactionUiState())
    val uiState: StateFlow<TransactionUiState> = _uiState.asStateFlow()

    private val _createTransactionResult = MutableStateFlow<ApiResult<TransactionResponse>?>(null)
    val createTransactionResult: StateFlow<ApiResult<TransactionResponse>?> = _createTransactionResult

    private val _categories = MutableStateFlow<List<CategoryItem>>(emptyList())
    val categories: StateFlow<List<CategoryItem>> = _categories.asStateFlow()

    private val _showCategoryDialog = MutableStateFlow(false)
    val showCategoryDialog: StateFlow<Boolean> = _showCategoryDialog.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _categories.value = repository.getAllCategories().map { it.toDomain().toCategoryItem() }
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
        accountId: Int = accountManager.selectedAccountId,
        categoryId: String,
        amount: String,
        transactionDate: String,
        comment: String? = null
    ) {
        viewModelScope.launch {
            _createTransactionResult.value = apiCallHelper.safeApiCall(
                block = {
                    repository.createTransaction(
                        accountId = accountId,
                        categoryId = categoryId,
                        amount = amount,
                        transactionDate = transactionDate,
                        comment = comment
                    )
                }
            )
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
