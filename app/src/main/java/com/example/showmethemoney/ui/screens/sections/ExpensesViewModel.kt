package com.example.showmethemoney.ui.screens.sections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.showmethemoney.data.AccountManager
import com.example.showmethemoney.data.FinanceRepository
import com.example.showmethemoney.data.dto.account.toDomain
import com.example.showmethemoney.data.dto.category.toDomain
import com.example.showmethemoney.data.dto.transaction.toDomain
import com.example.showmethemoney.data.safecaller.ApiError
import com.example.showmethemoney.data.safecaller.ApiResult
import com.example.showmethemoney.data.safecaller.NetworkMonitor
import com.example.showmethemoney.data.safecaller.safeApiCall
import com.example.showmethemoney.domain.utils.toExpenseItem
import com.example.showmethemoney.domain.utils.toIncomeItem
import com.example.showmethemoney.ui.components.ExpenseItem
import com.example.showmethemoney.ui.components.IncomeItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject constructor(
    private val repository: FinanceRepository,
    private val networkMonitor: NetworkMonitor,
    private val accountManager: AccountManager
) : ViewModel() {

    private val _expenses = MutableStateFlow<ApiResult<List<ExpenseItem>>>(ApiResult.Loading)
    val expenses: StateFlow<ApiResult<List<ExpenseItem>>> = _expenses

    private val _incomes = MutableStateFlow<ApiResult<List<IncomeItem>>>(ApiResult.Loading)
    val incomes: StateFlow<ApiResult<List<IncomeItem>>> = _incomes

    val currentDate = formatCurrentDate()

    fun loadTransactions(startDate: String? = null, endDate: String? = null, isIncome: Boolean) {
        viewModelScope.launch {
            val accountId = accountManager.selectedAccountId
            if (accountId == -1) {
                val error = ApiError.UnknownError("Account not selected")
                _expenses.value = ApiResult.Error(error)
                _incomes.value = ApiResult.Error(error)
                return@launch
            }

            val finalStartDate = startDate ?: getFirstDayOfCurrentMonth()
            val finalEndDate = endDate ?: getLastDayOfCurrentMonth()

            if (isIncome) {
                _incomes.value = ApiResult.Loading
                _incomes.value = safeApiCall(
                    networkMonitor = networkMonitor,
                    block = {
                        repository.getTransactions(accountId, finalStartDate, finalEndDate)
                            .filter { it.category.isIncome }
                            .map { transaction ->
                                transaction.toDomain()
                                    .toIncomeItem(
                                        transaction.category.toDomain(),
                                        transaction.account.toDomain()
                                    )
                            }
                    }
                )
            } else {
                _expenses.value = ApiResult.Loading
                _expenses.value = safeApiCall(
                    networkMonitor = networkMonitor,
                    block = {
                        repository.getTransactions(accountId, finalStartDate, finalEndDate)
                            .filter { !it.category.isIncome }
                            .map { transaction ->
                                transaction.toDomain()
                                    .toExpenseItem(
                                        transaction.category.toDomain(),
                                        transaction.account.toDomain()
                                    )
                            }
                    }
                )
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
    private fun formatDate(date: Date): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
    }
    private fun getLastDayOfCurrentMonth(): String {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
        }
        return formatDate(calendar.time)
    }
}