package com.example.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import com.example.ui.UniversalListItem
import com.example.ui.theme.Indicator
import com.example.utils.AccountManager
import com.example.utils.StringFormatter

/**
 * Отображает список транзакций с заголовком, содержащим общую сумму.
 *
 * Этот компонент представляет собой прокручиваемый список транзакций с:
 * - Заголовком, отображающим сумму всех транзакций
 * - Элементами списка для каждой отдельной транзакции
 *
 * @param transactions Список объектов [TransactionItem] для отображения, где каждый элемент содержит:
 *   - Категорию (с эмодзи)
 *   - Название категории
 *   - Комментарий (опционально)
 *   - Сумму и валюту
 *   - Дополнительные действия (кнопка "ещё")
 *
 * @see UniversalListItem Базовый компонент для отображения элементов списка
 * @see com.example.utils.StringFormatter Утилита для форматирования денежных сумм
 */
@Composable
fun TransactionList(transactions: List<TransactionItem>,
                    navController: NavController,
                    isIncome: Boolean) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            UniversalListItem(
                content = stringResource(com.example.ui.R.string.amount) to null,
                trail = StringFormatter.formatAmount(
                    transactions.sumOf { it.amount.toDoubleOrNull() ?: 0.0 },
                    transactions.firstOrNull()?.accountCurrency
                        ?: AccountManager.selectedAccountCurrency.value
                ) to null,
                modifier = Modifier
                    .background(Indicator)
                    .height(56.dp)
            )
        }
        items(transactions) { item ->
            UniversalListItem(
                lead = item.categoryEmoji,
                content = item.categoryName to item.comment,
                trail = StringFormatter.formatAmount(item.amount.toDouble(), item.accountCurrency)  to {
                    Icon(
                        painter = painterResource(com.example.ui.R.drawable.ic_more_vert),
                        contentDescription = stringResource(com.example.ui.R.string.more),
                        modifier = Modifier.size(24.dp)
                    )
                },
                onClick = { navController.navigate(if(isIncome) "add_income?transactionId=${item.id}"
                else "add_expense?transactionId=${item.id}") }
            )
        }
    }
}