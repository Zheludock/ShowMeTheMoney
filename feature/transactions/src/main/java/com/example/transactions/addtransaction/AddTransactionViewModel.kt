package com.example.transactions.addtransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.CategoryDomain
import com.example.domain.model.TransactionDomain
import com.example.domain.response.ApiResult
import com.example.domain.usecase.CreateTransactionUseCase
import com.example.domain.usecase.DeleteTransactionUseCase
import com.example.domain.usecase.GetCategoriesByTypeUseCase
import com.example.domain.usecase.GetTransactionDetailsUseCase
import com.example.domain.usecase.UpdateTransactionUseCase
import com.example.ui.AccountManager
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

class AddTransactionViewModel @Inject constructor(
    private val getCategoriesByTypeUseCase: GetCategoriesByTypeUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val createTransactionUseCase: CreateTransactionUseCase,
    private val getTransactionDetailsUseCase: GetTransactionDetailsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(TransactionUiState(
        selectedAccountId = AccountManager.selectedAccountId,
        accountName = AccountManager.selectedAccountName.value,
        selectedCategoryId = -1,
        categoryName = "",
        amount = "",
        transactionDate = "",
        comment = ""
    ))
    val uiState: StateFlow<TransactionUiState> = _uiState.asStateFlow()

    private val _categories = MutableStateFlow<ApiResult<List<CategoryDomain>>>(ApiResult.Loading)
    val categories: StateFlow<ApiResult<List<CategoryDomain>>> = _categories

    private val _showCategoryDialog = MutableStateFlow(false)
    val showCategoryDialog: StateFlow<Boolean> = _showCategoryDialog.asStateFlow()

    private val _isIncome = MutableStateFlow(false)
    val isIncome: StateFlow<Boolean> = _isIncome

    private val _currentTransaction = MutableStateFlow<ApiResult<TransactionDomain>?>(null)
    val currentTransaction: StateFlow<ApiResult<TransactionDomain>?> = _currentTransaction.asStateFlow()

    fun updateIsIncome(isIncome: Boolean) {
        _isIncome.value = isIncome
    }

    fun loadCurrentTransaction(transactionId: Int?) {
        if (transactionId == null) {
            _currentTransaction.value = null
            return
        }

        viewModelScope.launch {
            _currentTransaction.value = ApiResult.Loading
            when(val result = getTransactionDetailsUseCase.execute(transactionId)) {
                is ApiResult.Success -> {
                    _currentTransaction.value = ApiResult.Success(result.data)
                }
                else -> {
                    _currentTransaction.value = result
                }
            }
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            _categories.value = ApiResult.Loading
            when (val result = getCategoriesByTypeUseCase.execute(isIncome.value)) {
                is ApiResult.Success -> {
                    _categories.value = ApiResult.Success(result.data)
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

    fun updateSelectedCategory(categoryId: Int, categoryName: String) {
        _uiState.update { it.copy(
            selectedCategoryId = categoryId,
            categoryName = categoryName
        ) }
        showCategoryDialog(false)
    }

    fun createTransaction(
        accountId: Int = AccountManager.selectedAccountId,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String? = null
    ) {
        viewModelScope.launch {
            createTransactionUseCase.execute(
                accountId = accountId,
                categoryId = categoryId,
                amount = amount,
                transactionDate = transactionDate,
                comment = comment
            )
        }
    }

    fun updateTransaction(id: Int,
                          categoryId: Int,
                          amount: String,
                          date: String,
                          comment: String?){
        viewModelScope.launch {
            updateTransactionUseCase.execute(
                id = id,
                accountId = AccountManager.selectedAccountId,
                categoryId = categoryId,
                amount = amount,
                date = date.toString(),
                comment = comment
            )
        }
    }

    fun deleteTransaction(id: Int){
        viewModelScope.launch {
            deleteTransactionUseCase.execute(id)
        }
    }

    fun updateAmount(amount: String) {
        _uiState.update { it.copy(amount = amount) }
    }

    fun updateTransactionDate(date: String) {
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

    fun setCurrentDate() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        _uiState.update { it.copy(transactionDate = dateFormat.format(Date())) }
    }
}
