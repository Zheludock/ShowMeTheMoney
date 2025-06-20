package com.example.showmethemoney.ui.screens.sections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.showmethemoney.data.AccountManager
import com.example.showmethemoney.data.FinanceRepository
import com.example.showmethemoney.data.dto.account.toDomain
import com.example.showmethemoney.data.dto.category.toDomain
import com.example.showmethemoney.data.dto.transaction.toDomain
import com.example.showmethemoney.data.safecaller.ApiCallHelper
import com.example.showmethemoney.data.safecaller.ApiError
import com.example.showmethemoney.data.safecaller.ApiResult
import com.example.showmethemoney.domain.utils.toExpenseItem
import com.example.showmethemoney.domain.utils.toIncomeItem
import com.example.showmethemoney.ui.utils.ExpenseItem
import com.example.showmethemoney.ui.utils.IncomeItem
import com.example.showmethemoney.ui.utils.formatDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject constructor(
    private val repository: FinanceRepository,
    private val apiCallHelper: ApiCallHelper,
    private val accountManager: AccountManager
): ViewModel() {

    private val _expenses = MutableStateFlow<ApiResult<List<ExpenseItem>>>(ApiResult.Loading)
    val expenses: StateFlow<ApiResult<List<ExpenseItem>>> = _expenses

    private val _incomes = MutableStateFlow<ApiResult<List<IncomeItem>>>(ApiResult.Loading)
    val incomes: StateFlow<ApiResult<List<IncomeItem>>> = _incomes

    val currentDate = formatCurrentDate()

    private val _startDateForUI = MutableStateFlow(getFirstDayOfCurrentMonth())
    val startDateForUI: StateFlow<String> = _startDateForUI.asStateFlow()

    private val _endDateForUI = MutableStateFlow(currentDate)
    val endDateForUI: StateFlow<String> = _endDateForUI.asStateFlow()

    fun loadTransactions(isIncome: Boolean) {
        viewModelScope.launch {
            val accountId = accountManager.selectedAccountId
            if (accountId == -1) {
                val error = ApiError.UnknownError("Account not selected")
                _expenses.value = ApiResult.Error(error)
                _incomes.value = ApiResult.Error(error)
                return@launch
            }

            val finalStartDate = _startDateForUI.value
            val finalEndDate = _endDateForUI.value

            if (isIncome) {
                _incomes.value = ApiResult.Loading
                _incomes.value = apiCallHelper.safeApiCall(
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
                            .sortedByDescending { it.createdAt }
                    },
                )
            } else {
                _expenses.value = ApiResult.Loading
                _expenses.value = apiCallHelper.safeApiCall(
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
                            .sortedByDescending { it.createdAt }
                    },
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

    fun updateStartDate(dateString: String) {
        _startDateForUI.value = dateString
    }

    fun updateEndDate(dateString: String) {
        _endDateForUI.value = dateString
    }
}