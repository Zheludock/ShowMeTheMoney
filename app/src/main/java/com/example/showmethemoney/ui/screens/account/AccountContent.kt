package com.example.showmethemoney.ui.screens.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import com.example.showmethemoney.R
import com.example.showmethemoney.ui.components.UniversalListItem
import com.example.showmethemoney.ui.theme.Indicator
import com.example.showmethemoney.ui.utils.AccountManager
import com.example.showmethemoney.ui.utils.StringFormatter

/**
 * Компонент для отображения основной информации о счете:
 * - Текущий баланс
 * - Валюта счета
 *
 * @param history Данные счета для отображения
 */
@Composable
fun AccountContent(
    history: AccountDetailsItem,
    viewModel: AccountViewModel,
    navBackStackEntry: NavBackStackEntry
) {
    var showChangeCurrencyModal by remember { mutableStateOf(false) }

    var showChangeNameDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            UniversalListItem(
                lead = "💰",
                content = stringResource(R.string.balance) to null,
                trail = StringFormatter.formatAmount(history.balance.toDouble(),
                    history.currency) to null,
                modifier = Modifier
                    .background(Indicator)
                    .height(56.dp)
                    .padding(bottom = 1.dp),
                onClick = {}
            )
        }
        item {
            UniversalListItem(
                content = stringResource(R.string.currency) to null,
                trail = StringFormatter.getCurrencySymbol(history.currency) to {
                    Icon(
                        painter = painterResource(R.drawable.ic_more_vert),
                        contentDescription = stringResource(R.string.more),
                        modifier = Modifier.size(24.dp)
                    )
                },
                modifier = Modifier
                    .background(Indicator)
                    .height(56.dp),
                onClick = { showChangeCurrencyModal = true }
            )
        }
    }

    if (showChangeCurrencyModal) {
        ChangeCurrencyModal(
            onDismissRequest = { showChangeCurrencyModal = false },
            onElementSelected = { currency ->
                viewModel.updateCurrency(currency, history.balance)
                showChangeCurrencyModal = false
            }
        )
    }

    if (showChangeNameDialog) {
        EditAccountNameDialog(
            initialName = AccountManager.selectedAccountName,
            onDismissRequest = { showChangeNameDialog = false },
            onConfirm = { newName ->
                viewModel.updateName(newName, history.balance)
                showChangeNameDialog = false
            }
        )
    }
}
