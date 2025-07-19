package com.example.editexpence

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.CategoryDomain
import com.example.domain.model.TransactionDomain
import com.example.domain.usecase.category.GetCategoriesByTypeUseCase
import com.example.domain.usecase.transaction.DeleteTransactionUseCase
import com.example.domain.usecase.transaction.GetTransactionDetailsUseCase
import com.example.domain.usecase.transaction.UpdateTransactionUseCase
import com.example.ui.EditTransactionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditExpenseViewModel @Inject constructor(
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val getCategoriesByTypeUseCase: GetCategoriesByTypeUseCase,
    private val getTransactionDetailsUseCase: GetTransactionDetailsUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase
) : ViewModel() {
    private val _transactionEdited = MutableSharedFlow<Unit>()
    val transactionEdited: SharedFlow<Unit> = _transactionEdited.asSharedFlow()

    private val _expenseCategories = MutableStateFlow<List<CategoryDomain>>(emptyList())
    val expenseCategories: StateFlow<List<CategoryDomain>> = _expenseCategories.asStateFlow()

    private val _currentTransaction = MutableStateFlow<TransactionDomain?>(null)
    val currentTransaction: StateFlow<TransactionDomain?> = _currentTransaction

    var state by mutableStateOf(EditTransactionState())
        private set

    init {
        loadCategories()
    }

    fun loadTransactionById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val transaction = getTransactionDetailsUseCase.execute(id)
            _currentTransaction.value = transaction
            transaction?.let {
                state = state.copy(
                    id = it.id,
                    categoryId = it.categoryId,
                    categoryName = it.categoryName,
                    amount = it.amount,
                    transactionDate = it.transactionDate,
                    comment = it.comment
                )
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            _expenseCategories.value = getCategoriesByTypeUseCase.execute(false)
        }
    }

    fun onCategorySelected(categoryId: Int, categoryName: String) {
        state = state.copy(categoryId = categoryId, categoryName = categoryName)
    }

    fun onAmountChange(amount: String) {
        state = state.copy(amount = amount)
    }

    fun updateDate(newDate: String) {
        state = state.copy(
            transactionDate = newDate
        )
    }

    fun onCommentChange(comment: String) {
        state = state.copy(comment = comment)
    }

    fun resetState() {
        state = EditTransactionState()
    }

    fun editTransaction() {
        Log.d("EDITTRANSACTION", "Получил команду на редактирование")
        val accountId = state.accountId
        val categoryId = state.categoryId
        val amount = state.amount
        val date = state.transactionDate

        if (amount.isBlank()) {
            return
        }
        val amountParsed = amount.replace(',', '.').toDoubleOrNull()
        if (amountParsed == null || amountParsed <= 0.0) {
            return
        }

        Log.d("EDITTRANSACTION", "Валидация прошла, отдаю команду юзкейзу")
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("EDITTRANSACTION", "Валидация прошла, отдаю команду юзкейзу")
            updateTransactionUseCase.execute(
                id = state.id,
                accountId = accountId,
                categoryId = categoryId,
                amount = amount,
                date = date,
                comment = state.comment
            )
            _transactionEdited.emit(Unit)
            resetState()
        }
    }

    fun deleteExpense(){
        viewModelScope.launch(Dispatchers.IO) {
            deleteTransactionUseCase.execute(state.id)
            _transactionEdited.emit(Unit)
            resetState()
        }
    }
}