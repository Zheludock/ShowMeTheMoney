package com.example.showmethemoney.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.showmethemoney.R
import com.example.showmethemoney.ui.theme.Indicator
import com.example.showmethemoney.ui.utils.TransactionItem
import com.example.showmethemoney.ui.utils.formatAmount
import com.example.showmethemoney.ui.utils.formatDate
import com.example.showmethemoney.ui.utils.formatDateForDisplay
import com.example.showmethemoney.ui.utils.formatTime
import com.example.showmethemoney.ui.utils.stringToDate

//Доработать
@Composable
fun TransactionHistoryList(
    transactions: List<TransactionItem>,
    startDate: String,
    endDate: String,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            UniversalListItem(
                content = "Начало" to null,
                trail = startDate to null,
                modifier = Modifier
                    .background(Indicator)
                    .height(56.dp)
                    .padding(bottom = 1.dp),
                onClick = { onStartDateClick() }
            )
        }
        item {
            UniversalListItem(
                content = "Конец" to null,
                trail = endDate to null,
                modifier = Modifier
                    .background(Indicator)
                    .height(56.dp)
                    .padding(bottom = 1.dp),
                onClick = { onEndDateClick() }
            )
        }
        item {
            UniversalListItem(
                content = "Сумма" to null,
                trail = formatAmount(transactions.sumOf { it.amount.toDoubleOrNull() ?: 0.0 },
                    transactions.firstOrNull()?.accountCurrency ?: "RUB") to null,
                modifier = Modifier
                    .background(Indicator)
                    .height(56.dp)
                    .padding(bottom = 1.dp)
            )
        }
        items(transactions) { item ->
            UniversalListItem(
                lead = item.categoryEmoji,
                content = item.categoryName to item.comment,
                trail = buildString {
                    append(formatAmount(item.amount.toDoubleOrNull() ?: 0.0, item.accountCurrency))
                    append("\n")
                    item.createdAt?.let { createdAt ->
                        stringToDate(createdAt)?.let { date ->
                            append(formatDateForDisplay(formatDate(date)))
                            append(" ${formatTime(date)}")
                        }
                    }
                } to {
                    Icon(
                        painter = painterResource(R.drawable.ic_more_vert),
                        contentDescription = "Подробнее",
                        modifier = Modifier.size(24.dp)
                    )
                }
            )
        }
    }
}