package com.example.transactions.transactionhistory

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.transactions.TransactionItem
import com.example.ui.AccountManager
import com.example.ui.DateUtils.formatDate
import com.example.ui.DateUtils.formatDateForDisplay
import com.example.ui.DateUtils.formatTime
import com.example.ui.DateUtils.stringToDate
import com.example.ui.R
import com.example.ui.StringFormatter.formatAmount
import com.example.ui.UniversalListItem
import com.example.ui.theme.Indicator

/**
 * Компонент для отображения истории транзакций с возможностью выбора периода и подсчётом суммы.
 *
 * Отображает:
 * 1. Две строки выбора дат (начала и конца периода)
 * 2. Строку с общей суммой транзакций за выбранный период
 * 3. Список всех транзакций в выбранном периоде
 *
 * @param transactions Список транзакций для отображения
 * @param startDate Текстовая дата начала периода
 * @param endDate Текстовая дата конца периода
 * @param onStartDateClick Обработчик нажатия на поле даты начала периода
 * @param onEndDateClick Обработчик нажатия на поле даты конца периода
 *
 * Особенности реализации:
 * - Использует [androidx.compose.foundation.lazy.LazyColumn] для эффективного рендеринга длинных списков
 * - Первые три элемента фиксированные (даты и сумма)
 * - Каждая транзакция отображается через [com.example.showmethemoney.ui.components.UniversalListItem] с:
 * - Эмодзи категории слева
 * - Названием категории и комментарием(опционально) в основном содержимом
 * - Отформатированной суммой и кнопкой действий справа
 * - Даты и общая сумма имеют особый стиль (фон [com.example.showmethemoney.ui.theme.Indicator])
 * - Форматирование сумм выполняется через [com.example.ui.StringFormatter]
 */
@Composable
fun TransactionHistoryList(
    transactions: List<TransactionItem>,
    navController: NavController,
    isIncome: Boolean,
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
                content = stringResource(R.string.start) to null,
                trail = startDate to null,
                modifier = Modifier
                    .background(Indicator)
                    .height(56.dp)
                    .padding(bottom = 1.dp),
                onClick = onStartDateClick
            )
        }
        item {
            UniversalListItem(
                content = stringResource(R.string.end) to null,
                trail = endDate to null,
                modifier = Modifier
                    .background(Indicator)
                    .height(56.dp)
                    .padding(bottom = 1.dp),
                onClick = onEndDateClick
            )
        }
        item {
            UniversalListItem(
                content = stringResource(R.string.sum) to null,
                trail = formatTotalAmount(transactions) to null,
                modifier = Modifier
                    .background(Indicator)
                    .height(56.dp)
                    .padding(bottom = 1.dp)
            )
        }
        items(transactions) { item ->
            val trailText = formatTransactionTrail(item)
            UniversalListItem(
                lead = item.categoryEmoji,
                content = item.categoryName to item.comment,
                trail = trailText to {
                    Icon(
                        painter = painterResource(R.drawable.ic_more_vert),
                        contentDescription = stringResource(R.string.more),
                        modifier = Modifier.size(24.dp)
                    )
                },
                onClick = { navController.navigate(if(isIncome) "add_income?transactionId=${item.id}"
                else "add_expense?transactionId=${item.id}") }
            )
        }
    }
}

fun formatTotalAmount(transactions: List<TransactionItem>): String {
    val total = transactions.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }

    val currency = transactions.firstOrNull()?.accountCurrency
        ?: AccountManager.selectedAccountCurrency.value

    return formatAmount(total, currency)
}

fun formatTransactionTrail(item: TransactionItem): String {
    val amountStr = formatAmount(item.amount.toDoubleOrNull()
        ?: 0.0, AccountManager.selectedAccountCurrency.value)
    val dateStr = item.createdAt.let { createdAt ->
        stringToDate(createdAt)?.let { date ->
            "${formatDateForDisplay(formatDate(date))} ${formatTime(date)}"
        } ?: ""
    }
    val trailText = buildString {
        append(amountStr)
        append("\n")
        append(dateStr)
    }
    return trailText
}