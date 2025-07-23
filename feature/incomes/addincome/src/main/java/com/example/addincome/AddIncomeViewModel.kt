package com.example.addincome

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.CategoryDomain
import com.example.domain.usecase.category.GetCategoriesByTypeUseCase
import com.example.domain.usecase.transaction.CreateTransactionUseCase
import com.example.ui.AddTransactionState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

class AddIncomeViewModel @Inject constructor(
    private val createTransactionUseCase: CreateTransactionUseCase,
    private val getCategoriesByTypeUseCase: GetCategoriesByTypeUseCase
) : ViewModel() {

    private val _transactionCreated = MutableSharedFlow<Unit>()
    val transactionCreated: SharedFlow<Unit> = _transactionCreated.asSharedFlow()

    private val _incomeCategories = MutableStateFlow<List<CategoryDomain>>(emptyList())
    val incomeCategories: StateFlow<List<CategoryDomain>> = _incomeCategories.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _incomeCategories.value = getCategoriesByTypeUseCase.execute(true)
        }
    }

    var state by mutableStateOf(AddTransactionState())
        private set


    fun onCategorySelected(categoryId: Int, categoryName: String) {
        state = state.copy(categoryId = categoryId, categoryName = categoryName)
    }

    fun onAmountChange(amount: String) {
        state = state.copy(amount = amount)
    }

    fun updateDate(newDate: Date) {
        state = state.copy(
            transactionDate = newDate
        )
    }

    fun onCommentChange(comment: String) {
        state = state.copy(comment = comment)
    }

    fun resetState() {
        state = AddTransactionState()
    }

    fun createTransaction() {
        val amount = state.amount

        if (amount.isBlank()) {
            return
        }
        val amountParsed = amount.replace(',', '.').toDoubleOrNull()
        if (amountParsed == null || amountParsed <= 0.0) {
            return
        }

        viewModelScope.launch {
            createTransactionUseCase.execute(
                accountId = state.accountId,
                categoryId = state.categoryId,
                amount = amount,
                transactionDate = state.transactionDate,
                comment = state.comment
            )
            _transactionCreated.emit(Unit)
            resetState()
        }
    }
}