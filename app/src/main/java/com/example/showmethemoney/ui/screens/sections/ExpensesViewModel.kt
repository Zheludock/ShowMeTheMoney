package com.example.showmethemoney.ui.screens.sections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.ApiResult
import com.example.domain.usecase.GetTransactionsUseCase
import com.example.showmethemoney.ui.utils.AccountManager
import com.example.showmethemoney.ui.utils.TransactionItem
import com.example.showmethemoney.ui.utils.formatDate
import com.example.showmethemoney.ui.utils.toTransactionItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
//Доработать
class ExpensesViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel() {

    private val _expenses = MutableStateFlow<ApiResult<List<TransactionItem>>>(ApiResult.Loading)
    val expenses: StateFlow<ApiResult<List<TransactionItem>>> = _expenses

    private val _incomes = MutableStateFlow<ApiResult<List<TransactionItem>>>(ApiResult.Loading)
    val incomes: StateFlow<ApiResult<List<TransactionItem>>> = _incomes

    val currentDate = formatCurrentDate()


    private val _startDateForUI = MutableStateFlow(getFirstDayOfCurrentMonth())
    val startDateForUI: StateFlow<String> = _startDateForUI

    private val _endDateForUI = MutableStateFlow(currentDate)
    val endDateForUI: StateFlow<String> = _endDateForUI

    fun loadTransactions(isIncome: Boolean) {
        viewModelScope.launch {
            val accountId = AccountManager.selectedAccountId.toString()

            if (isIncome) {
                _incomes.value = ApiResult.Loading
            } else {
                _expenses.value = ApiResult.Loading
            }

            when (val result = getTransactionsUseCase.execute(
                accountId = accountId,
                startDate = _startDateForUI.value,
                endDate = _endDateForUI.value
            )) {
                is ApiResult.Success -> {
                    val mappedItems = result.data.map { it.toTransactionItem() }
                        .filter { it.isIncome == isIncome }
                        .sortedByDescending { it.createdAt }
                    if (isIncome) {
                        _incomes.value = ApiResult.Success(mappedItems)
                    } else {
                        _expenses.value = ApiResult.Success(mappedItems)
                    }
                }
                is ApiResult.Error -> {
                    if (isIncome) {
                        _incomes.value = result
                    } else {
                        _expenses.value = result
                    }
                }
                ApiResult.Loading -> Unit
            }
        }
    }

    private fun getFirstDayOfCurrentMonth(): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        return formatDate(calendar.time)
    }

    private fun formatCurrentDate(): String {
        return formatDate(Date())
    }

    fun updateStartDate(dateString: String) {
        _startDateForUI.value = dateString
    }

    fun updateEndDate(dateString: String) {
        _endDateForUI.value = dateString
    }
}