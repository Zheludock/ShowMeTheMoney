package com.example.showmethemoney.ui.screens.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.showmethemoney.R
import com.example.showmethemoney.data.safecaller.ApiResult
import com.example.showmethemoney.ui.components.IncomeItem
import com.example.showmethemoney.ui.components.UniversalListItem
import com.example.showmethemoney.ui.theme.Indicator

@Composable
fun IncomeScreen(viewModel: ExpensesViewModel = hiltViewModel()) {
    viewModel.loadTransactions(startDate = viewModel.currentDate, isIncome = true)
    val incomesState by viewModel.incomes.collectAsState()

    when (val state = incomesState) {
        is ApiResult.Loading -> LoadingIndicator()
        is ApiResult.Success -> IncomeList(state.data)
        is ApiResult.Error -> ErrorView(state.error) {
            viewModel.loadTransactions(isIncome = true)
        }
    }
}

@Composable
private fun IncomeList(incomes: List<IncomeItem>){
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            UniversalListItem(
                content = "Всего" to null,
                trail = "50000 ₽" to null,
                modifier = Modifier
                    .background(Indicator)
                    .height(56.dp)
            )
        }
        items(incomes) { item ->
            UniversalListItem(
                content = item.categoryName to item.comment,
                trail = (item.amount + " " + item.accountCurrency) to {
                    IconButton(
                        onClick = { /* TODO */ },
                        modifier = Modifier.size(24.dp)
                            .wrapContentSize()
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_more_vert),
                            contentDescription = "Подробнее",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            )
        }
    }
}