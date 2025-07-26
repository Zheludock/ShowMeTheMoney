package com.example.editincome

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.CategoryDomain
import com.example.domain.usecase.category.GetCategoriesByTypeUseCase
import com.example.domain.usecase.transaction.DeleteTransactionUseCase
import com.example.domain.usecase.transaction.UpdateTransactionUseCase
import com.example.ui.EditTransactionState
import com.example.ui.TransactionTransfer
import com.example.utils.AccountManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

class EditIncomeViewModel @Inject constructor(
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val getCategoriesByTypeUseCase: GetCategoriesByTypeUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase
) : ViewModel() {

    private val _transactionEdited = MutableSharedFlow<Unit>()
    val transactionEdited: SharedFlow<Unit> = _transactionEdited.asSharedFlow()

    private val _expenseCategories = MutableStateFlow<List<CategoryDomain>>(emptyList())
    val expenseCategories: StateFlow<List<CategoryDomain>> = _expenseCategories.asStateFlow()

    var state by mutableStateOf(EditTransactionState())
        private set

    init {
        loadCategories()
        resetState()
    }

    private fun loadCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            _expenseCategories.value = getCategoriesByTypeUseCase.execute(true)
        }
    }

    fun onCategorySelected(categoryId: Int, categoryName: String) {
        state = state.copy(categoryId = categoryId, categoryName = categoryName)
    }

    fun onAmountChange(amount: String) {
        state = state.copy(amount = amount)
    }

    fun updateDate(newDate: Date) {
        state = state.copy(transactionDate = newDate)
    }

    fun onCommentChange(comment: String) {
        state = state.copy(comment = comment)
    }

    fun resetState() {
        val transaction = TransactionTransfer.toDomain()
        state = EditTransactionState(
            id = transaction.id,
            accountId = AccountManager.selectedAccountId,
            categoryId = transaction.categoryId,
            categoryName = transaction.categoryName,
            amount = transaction.amount,
            transactionDate = transaction.transactionDate,
            comment = transaction.comment
        )
    }

    fun editTransaction() {
        val accountId = state.accountId
        val categoryId = state.categoryId
        val amountStr = state.amount
        val date = state.transactionDate

        if (amountStr.isBlank()) return

        val amountParsed = amountStr.replace(',', '.').toDoubleOrNull()
        if (amountParsed == null || amountParsed <= 0.0) return

        viewModelScope.launch(Dispatchers.IO) {
            updateTransactionUseCase.execute(
                id = state.id,
                accountId = accountId,
                categoryId = categoryId,
                amount = amountStr,
                date = date,
                comment = state.comment
            )
            _transactionEdited.emit(Unit)
            resetState()
        }
    }

    fun deleteIncome() {
        viewModelScope.launch(Dispatchers.IO) {
            deleteTransactionUseCase.execute(state.id)
            _transactionEdited.emit(Unit)
            resetState()
        }
    }
}