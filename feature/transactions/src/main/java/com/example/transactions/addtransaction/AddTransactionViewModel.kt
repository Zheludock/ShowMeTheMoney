package com.example.transactions.addtransaction

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.CategoryDomain
import com.example.domain.model.TransactionDomain
import com.example.domain.response.ApiResult
import com.example.domain.usecase.category.GetCategoriesByTypeUseCase
import com.example.domain.usecase.transaction.CreateTransactionUseCase
import com.example.domain.usecase.transaction.DeleteTransactionUseCase
import com.example.domain.usecase.transaction.GetTransactionDetailsUseCase
import com.example.domain.usecase.transaction.UpdateTransactionUseCase
import com.example.utils.AccountManager
import com.example.utils.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
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

    private val _incomeCategories = MutableStateFlow<ApiResult<List<CategoryDomain>>>(ApiResult.Loading)
    val incomeCategories: StateFlow<ApiResult<List<CategoryDomain>>> = _incomeCategories.asStateFlow()

    private val _expenseCategories = MutableStateFlow<ApiResult<List<CategoryDomain>>>(ApiResult.Loading)
    val expenseCategories: StateFlow<ApiResult<List<CategoryDomain>>> = _expenseCategories.asStateFlow()

    private val _showCategoryDialog = MutableStateFlow(false)
    val showCategoryDialog: StateFlow<Boolean> = _showCategoryDialog.asStateFlow()

    private val _isIncome = MutableStateFlow(false)
    val isIncome: StateFlow<Boolean> = _isIncome.asStateFlow()

    private val _currentTransaction = MutableStateFlow<ApiResult<TransactionDomain>?>(null)
    val currentTransaction: StateFlow<ApiResult<TransactionDomain>?> = _currentTransaction.asStateFlow()

    init {
        viewModelScope.launch {
            _isIncome.collectLatest { isIncome ->
                loadCategories(isIncome)
            }
        }
    }

    fun updateIsIncome(isIncome: Boolean) {
        _isIncome.value = isIncome
    }

    private suspend fun loadCategories(isIncome: Boolean) {
        if (isIncome) {
            loadIncomeCategories()
        } else {
            loadExpenseCategories()
        }
    }

    private suspend fun loadIncomeCategories() {
        _incomeCategories.value = ApiResult.Loading
        _incomeCategories.value = getCategoriesByTypeUseCase.execute(true)
    }

    private suspend fun loadExpenseCategories() {
        _expenseCategories.value = ApiResult.Loading
        _expenseCategories.value = getCategoriesByTypeUseCase.execute(false)
    }

    fun loadCurrentTransaction(transactionId: Int?) {
        if (transactionId == null) {
            _currentTransaction.value = null
            return
        }

        viewModelScope.launch {
            _currentTransaction.value = ApiResult.Loading
            _currentTransaction.value = getTransactionDetailsUseCase.execute(transactionId)
        }
    }

    fun showCategoryDialog(show: Boolean) {
        _showCategoryDialog.value = show
    }

    fun updateSelectedCategory(categoryId: Int, categoryName: String) {
        _uiState.update {
            it.copy(selectedCategoryId = categoryId, categoryName = categoryName)
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
            val result = createTransactionUseCase.execute(accountId, categoryId, amount, transactionDate, comment)
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
            val result = updateTransactionUseCase.execute(id, AccountManager.selectedAccountId, categoryId, amount, date, comment)
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
        _uiState.update { it.copy(transactionDate = DateUtils.formatToUtc(Date())) }
    }
}

