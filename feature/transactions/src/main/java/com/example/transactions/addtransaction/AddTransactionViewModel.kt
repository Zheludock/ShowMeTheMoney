package com.example.transactions.addtransaction

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.CategoryDomain
import com.example.domain.model.TransactionDomain
import com.example.domain.response.ApiResult
import com.example.domain.usecase.transaction.CreateTransactionUseCase
import com.example.domain.usecase.transaction.DeleteTransactionUseCase
import com.example.domain.usecase.category.GetCategoriesByTypeUseCase
import com.example.domain.usecase.transaction.GetTransactionDetailsUseCase
import com.example.domain.usecase.transaction.UpdateTransactionUseCase
import com.example.utils.AccountManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class AddTransactionViewModel @Inject constructor(
    private val getCategoriesByTypeUseCase: GetCategoriesByTypeUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val createTransactionUseCase: CreateTransactionUseCase,
    private val getTransactionDetailsUseCase: GetTransactionDetailsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        TransactionUiState(
            selectedAccountId = AccountManager.selectedAccountId,
            accountName = AccountManager.selectedAccountName.value,
            selectedCategoryId = -1,
            categoryName = "",
            amount = "",
            transactionDate = "",
            comment = ""
        )
    )
    val uiState: StateFlow<TransactionUiState> = _uiState.asStateFlow()

    private val _incomeCategories =
        MutableStateFlow<ApiResult<List<CategoryDomain>>>(ApiResult.Loading)
    val incomeCategories: StateFlow<ApiResult<List<CategoryDomain>>> =
        _incomeCategories.asStateFlow()

    private val _expenseCategories =
        MutableStateFlow<ApiResult<List<CategoryDomain>>>(ApiResult.Loading)
    val expenseCategories: StateFlow<ApiResult<List<CategoryDomain>>> =
        _expenseCategories.asStateFlow()

    private val _showCategoryDialog = MutableStateFlow(false)
    val showCategoryDialog: StateFlow<Boolean> = _showCategoryDialog.asStateFlow()

    private val _isIncome = MutableStateFlow(false)
    val isIncome: StateFlow<Boolean> = _isIncome.asStateFlow()

    private val _currentTransaction = MutableStateFlow<ApiResult<TransactionDomain>?>(null)
    val currentTransaction: StateFlow<ApiResult<TransactionDomain>?> =
        _currentTransaction.asStateFlow()

    init {
        viewModelScope.launch {
            _isIncome.collectLatest { income ->
                loadIncomeCategories()
                loadExpenseCategories()
            }
        }
    }

    fun updateIsIncome(isIncome: Boolean) {
        _isIncome.value = isIncome
    }

    private suspend fun loadIncomeCategories() {
        _incomeCategories.value = ApiResult.Loading
        when (val result = getCategoriesByTypeUseCase.execute(true)) {
            is ApiResult.Success -> _incomeCategories.value = ApiResult.Success(result.data)
            is ApiResult.Error -> _incomeCategories.value = result
            else -> Unit
        }
    }

    private suspend fun loadExpenseCategories() {
        _expenseCategories.value = ApiResult.Loading
        when (val result = getCategoriesByTypeUseCase.execute(false)) {
            is ApiResult.Success -> _expenseCategories.value = ApiResult.Success(result.data)
            is ApiResult.Error -> _expenseCategories.value = result
            else -> Unit
        }
    }

    fun loadCurrentTransaction(transactionId: Int?) {
        if (transactionId == null) {
            _currentTransaction.value = null
            return
        }

        viewModelScope.launch {
            _currentTransaction.value = ApiResult.Loading
            when (val result = getTransactionDetailsUseCase.execute(transactionId)) {
                is ApiResult.Success -> {
                    _currentTransaction.value = ApiResult.Success(result.data)
                }

                else -> {
                    _currentTransaction.value = result
                }
            }
        }
    }

    fun showCategoryDialog(show: Boolean) {
        _showCategoryDialog.value = show
    }

    fun updateSelectedCategory(categoryId: Int, categoryName: String) {
        _uiState.update {
            it.copy(
                selectedCategoryId = categoryId,
                categoryName = categoryName
            )
        }
        showCategoryDialog(false)
    }

    fun createTransaction(
        accountId: Int = AccountManager.selectedAccountId,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String? = null,
        onComplete: () -> Unit
    ) {
        viewModelScope.launch {
            val result = createTransactionUseCase.execute(
                accountId = accountId,
                categoryId = categoryId,
                amount = amount,
                transactionDate = transactionDate,
                comment = comment
            )
            if (result is ApiResult.Success) {
                onComplete()
            } else {
                Log.e("AddTransaction", "Failed to create Transaction")
            }
        }
    }

    fun updateTransaction(
        id: Int,
        categoryId: Int,
        amount: String,
        date: String,
        comment: String?,
        onComplete: () -> Unit
    ) {
        viewModelScope.launch {
            val result = updateTransactionUseCase.execute(
                id = id,
                accountId = AccountManager.selectedAccountId,
                categoryId = categoryId,
                amount = amount,
                date = date,
                comment = comment
            )
            if (result is ApiResult.Success) {
                onComplete()
            } else {
                Log.e("AddTransaction", "Failed to update Transaction")
            }
        }
    }

    fun deleteTransaction(id: Int, onComplete: () -> Unit) {
        viewModelScope.launch {
            deleteTransactionUseCase.execute(id)
            onComplete()
        }
    }

    fun updateAmount(amount: String) {
        _uiState.update { it.copy(amount = amount) }
    }

    fun updateTransactionDate(date: String) {
        _uiState.update { it.copy(transactionDate = date) }
    }

    fun updateComment(comment: String) {
        _uiState.update { it.copy(comment = comment) }
    }

    fun setCurrentDate() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        _uiState.update { it.copy(transactionDate = dateFormat.format(Date())) }
    }
}
