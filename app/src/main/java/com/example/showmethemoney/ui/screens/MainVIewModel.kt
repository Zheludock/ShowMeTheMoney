package com.example.showmethemoney.ui.screens

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.showmethemoney.domain.utils.mockAccountDomains
import com.example.showmethemoney.domain.utils.mockCategoryDomains
import com.example.showmethemoney.domain.utils.mockExpens
import com.example.showmethemoney.domain.utils.toCategoryItem
import com.example.showmethemoney.domain.utils.toExpenseItem
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
        //loadIncomes()
        loadCategory()
    }

    private fun loadCategory() {
        viewModelScope.launch {
            val categoryItems = mockCategoryDomains.map { category ->
                category.toCategoryItem()
            }
            _categoryItems.clear()
            _categoryItems.addAll(categoryItems)
        }
    }

    private fun loadExpenses() {
        viewModelScope.launch {
            val expenseItems = mockExpens.map { expense ->
                val category = mockCategoryDomains.find { it.categoryId == expense.categoryId }
                    ?: throw IllegalStateException("Article not found")
                val accountDomain = mockAccountDomains.find { it.id == expense.accountId }
                    ?: throw IllegalStateException("AccountDomain not found")

                expense.toExpenseItem(category, accountDomain)
            }
            _expenseItems.clear()
            _expenseItems.addAll(expenseItems)
        }
    }

    /*private fun loadIncomes(){
        viewModelScope.launch {
            val incomeItems = mockIncomeDomains.map{ income ->
                val category = mockCategoryDomains.find { it.categoryId == income.categoryId }
                    ?: throw IllegalStateException("Category not found")
                val accountDomain = mockAccountDomains.find { it.id ==  income.accountId }
                    ?: throw IllegalStateException("AccountDomain not found")

                income.toIncomeItem(category, accountDomain)
            }
            _incomeItems.clear()
            _incomeItems.addAll(incomeItems)
        }
    }*/
}
