package com.example.showmethemoney.ui.screens

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.showmethemoney.domain.utils.mockAccounts
import com.example.showmethemoney.domain.utils.mockCategory
import com.example.showmethemoney.domain.utils.mockExpenses
import com.example.showmethemoney.domain.utils.mockIncomes
import com.example.showmethemoney.domain.utils.toCategoryItem
import com.example.showmethemoney.domain.utils.toExpenseItem
import com.example.showmethemoney.domain.utils.toIncomeItem
import com.example.showmethemoney.ui.components.CategoryItem
import com.example.showmethemoney.ui.components.ExpenseItem
import com.example.showmethemoney.ui.components.IncomeItem
import kotlinx.coroutines.launch

open class MainViewModel : ViewModel() {
    private val _expenseItems = mutableStateListOf<ExpenseItem>()
    val expenseItems: List<ExpenseItem> get() = _expenseItems
    private val _incomeItems = mutableStateListOf<IncomeItem>()
    val incomeItems: List<IncomeItem> get() = _incomeItems
    private val _categoryItems = mutableStateListOf<CategoryItem>()
    val categoryItems: List<CategoryItem> get() = _categoryItems

    init {
        loadExpenses()
        loadIncomes()
        loadCategory()
    }

    private fun loadCategory() {
        viewModelScope.launch {
            val categoryItems = mockCategory.map { category ->
                category.toCategoryItem()
            }
            _categoryItems.clear()
            _categoryItems.addAll(categoryItems)
        }
    }

    private fun loadExpenses() {
        viewModelScope.launch {
            val expenseItems = mockExpenses.map { expense ->
                // Ищем соответствующие данные по ID
                val category = mockCategory.find { it.categoryId == expense.categoryId }
                    ?: throw IllegalStateException("Article not found")
                val account = mockAccounts.find { it.id == expense.accountId }
                    ?: throw IllegalStateException("Account not found")

                expense.toExpenseItem(category, account)
            }
            _expenseItems.clear()
            _expenseItems.addAll(expenseItems)
        }
    }

    private fun loadIncomes(){
        viewModelScope.launch {
            val incomeItems = mockIncomes.map{ income ->
                val category = mockCategory.find { it.categoryId == income.categoryId }
                    ?: throw IllegalStateException("Article not found")
                val account = mockAccounts.find { it.id ==  income.accountId }
                    ?: throw IllegalStateException("Account not found")

                income.toIncomeItem(category, account)
            }
            _incomeItems.clear()
            _incomeItems.addAll(incomeItems)
        }
    }
}
