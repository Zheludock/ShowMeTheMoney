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
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _incomeCategories = MutableStateFlow<ApiResult<List<CategoryDomain>>>(ApiResult.Loading)
    val incomeCategories: StateFlow<ApiResult<List<CategoryDomain>>> = _incomeCategories.asStateFlow()

    private val _expenseCategories = MutableStateFlow<ApiResult<List<CategoryDomain>>>(ApiResult.Loading)
    val expenseCategories: StateFlow<ApiResult<List<CategoryDomain>>> = _expenseCategories.asStateFlow()

    private val _showCategoryDialog = MutableStateFlow(false)
    val showCategoryDialog: StateFlow<Boolean> = _showCategoryDialog.asStateFlow()

    private val _currentTransaction = MutableStateFlow<ApiResult<TransactionDomain>?>(null)
    val currentTransaction: StateFlow<ApiResult<TransactionDomain>?> = _currentTransaction.asStateFlow()

    private var currentJob: Job? = null

    fun updateIsIncome(isIncome: Boolean) {
        viewModelScope.launch {
            if (isIncome) {
                _incomeCategories.value = getCategoriesByTypeUseCase.execute(true)
            } else {
                _expenseCategories.value = getCategoriesByTypeUseCase.execute(false)
            }
        }
    }

    fun loadCurrentTransaction(transactionId: Int?) {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            if (transactionId == null) {
                _currentTransaction.value = null
                return@launch
            }

            _currentTransaction.value = ApiResult.Loading
            _currentTransaction.value = getTransactionDetailsUseCase.execute(transactionId)
        }
    }

    fun resetPendingOperations() {
        currentJob?.cancel()
        currentJob = null
    }

    fun updateFromTransaction(transaction: TransactionDomain) {
        _uiState.update {
            it.copy(
                amount = transaction.amount,
                comment = transaction.comment ?: "",
                transactionDate = transaction.createdAt,
                selectedCategoryId = transaction.categoryId,
                categoryName = transaction.categoryName
            )
        }
    }

    fun updateSelectedCategory(categoryId: Int, categoryName: String) {
        _uiState.update {
            it.copy(selectedCategoryId = categoryId, categoryName = categoryName)
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
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            try {
                updateTransactionUseCase.execute(id, AccountManager.selectedAccountId, categoryId, amount, date, comment)
                onComplete()
            } catch (e: Exception) {
                Log.e("AddTransaction", "Failed to update Transaction", e)
            }
        }
    }

    fun createTransaction(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?,
        onComplete: () -> Unit
    ) {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            try {
                createTransactionUseCase.execute(accountId, categoryId, amount, transactionDate, comment)
                onComplete()
            } catch (e: Exception) {
                Log.e("AddTransaction", "Failed to create Transaction", e)
            }
        }
    }

    fun deleteTransaction(id: Int, onComplete: () -> Unit) {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            try {
                deleteTransactionUseCase.execute(id)
                onComplete()
            } catch (e: Exception) {
                Log.e("AddTransaction", "Failed to delete Transaction", e)
            }
        }
    }

    fun showCategoryDialog(show: Boolean) {
        _showCategoryDialog.value = show
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

    override fun onCleared() {
        super.onCleared()
        currentJob?.cancel()
    }
}

