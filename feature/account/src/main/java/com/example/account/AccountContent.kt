package com.example.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import barchart.BarChart
import com.example.ui.UniversalListItem
import com.example.utils.AccountManager
import com.example.utils.StringFormatter

/**
 * Компонент для отображения основной информации о счете:
 * - Текущий баланс
 * - Валюта счета
 *
 * @param details Данные счета для отображения
 */
@Composable
fun AccountContent(
    details: AccountDetailsItem,
    viewModelFactory: ViewModelProvider.Factory,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            UniversalListItem(
                lead = "💰",
                content = stringResource(com.example.ui.R.string.balance) to null,
                trail = StringFormatter.formatAmount(details.balance.toDouble(),
                    details.currency) to null,
                modifier = Modifier.background(MaterialTheme.colorScheme.secondary)
                    .height(56.dp)
                    .padding(bottom = 1.dp)
            )
        }
        item {
            UniversalListItem(
                content = stringResource(com.example.ui.R.string.currency) to null,
                trail = StringFormatter.getCurrencySymbol(details.currency) to null,
                modifier = Modifier.background(MaterialTheme.colorScheme.secondary)
                    .height(56.dp)
            )
        }
        item {
            BarChart(viewModelFactory, AccountManager.selectedAccountId)
        }
    }
}
