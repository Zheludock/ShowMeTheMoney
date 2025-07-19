package com.example.account.editaccount

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.account.AccountDetailsItem
import com.example.domain.response.ApiResult
import com.example.ui.R
import com.example.ui.UniversalListItem
import com.example.ui.theme.DividerGray
import com.example.utils.AccountManager
import com.example.utils.StringFormatter
import com.example.utils.TopBarState

/**
 * Экран редактирования данных аккаунта.
 *
 * Позволяет пользователю:
 * 1. Изменять название аккаунта
 * 2. Редактировать баланс
 * 3. Выбирать валюту из модального окна
 * 4. Сохранять изменения или отменять редактирование
 *
 * Основные функции:
 * - Загрузка текущих данных аккаунта
 * - Валидация вводимого баланса
 * - Отображение ошибок через Snackbar
 * - Взаимодействие с ViewModel для сохранения изменений
 *
 * @param viewModelFactory Фабрика для создания ViewModel экрана
 * @param navController Контроллер навигации для управления переходами
 *
 * @see EditAccountViewModel Модель представления, управляющая бизнес-логикой экрана
 * @see ChangeCurrencyModal Модальное окно выбора валюты
 * @see com.example.showmethemoney.ui.components.UniversalListItem Универсальный элемент списка с настройкой контента
 */
@Composable
fun EditAccountScreen(
    viewModelFactory: ViewModelProvider.Factory,
    navController: NavController,
    updateTopBar: (TopBarState) -> Unit
) {
    val viewModel: EditAccountViewModel = viewModel(factory = viewModelFactory)

    var showChangeCurrencyModal by remember { mutableStateOf(false) }
    var showBalanceErrorSnackbar by remember { mutableStateOf(false) }

    val accountDetailsState by viewModel.accountDetails.collectAsState()

    var accountName by remember { mutableStateOf("") }
    var accountBalance by remember { mutableStateOf("") }
    var currentCurrency by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }

    val errorMessage = stringResource(R.string.niumber_error_message)

    LaunchedEffect(Unit) {
        updateTopBar(
            TopBarState(
                title = "Редактирование счета",
                onActionClick = {
                    if (!viewModel.isValidBalance(accountBalance)) {
                        showBalanceErrorSnackbar = true
                    } else {
                        val balance = accountBalance
                            .replace(",", ".")
                            .toBigDecimal()
                        viewModel.updateAccount(
                            accountName,
                            currentCurrency,
                            balance.toString()
                        )
                        navController.popBackStack()
                    }
                }
            )
        )
    }

    LaunchedEffect(Unit) {
        viewModel.loadAccountDetails()
    }

    LaunchedEffect(accountDetailsState) {
            val data = accountDetailsState
        data?.let { accountName = it.name }
        data?.let { accountBalance = it.balance }
        data?.let { currentCurrency = it.currency }
    }

    LaunchedEffect(showBalanceErrorSnackbar) {
        if (showBalanceErrorSnackbar) {
            snackbarHostState.showSnackbar(
                message = errorMessage,
                duration = SnackbarDuration.Short
            )
            showBalanceErrorSnackbar = false
        }
    }

    LaunchedEffect(accountDetailsState) {
        accountDetailsState?.let {
            accountName = it.name
            accountBalance = it.balance
            currentCurrency = it.currency
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            updateTopBar(
                TopBarState(
                    title = AccountManager.selectedAccountName.value,
                    onActionClick = { navController.navigate("edit_account") })
            )
        }
    }


    LazyColumn {
        item {
            UniversalListItem(
                lead = "\uD83D\uDCB8",
                content = stringResource(R.string.account_name) to null,
                trail = null to {
                    UpdateAccountTextField(
                        value = accountName,
                        onValueChange = { accountName = it }
                    )
                },
                modifier = Modifier.height(56.dp),
            )
        }
        item {
            UniversalListItem(
                lead = "💰",
                content = stringResource(R.string.balance) to null,
                trail = null to {
                    UpdateAccountTextField(
                        value = accountBalance,
                        onValueChange = { accountBalance = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                },
                modifier = Modifier.height(56.dp),
            )
        }
        item {
            UniversalListItem(
                content = stringResource(R.string.currency) to null,
                trail = StringFormatter.getCurrencySymbol(currentCurrency) to {
                    Icon(
                        painter = painterResource(R.drawable.ic_more_vert),
                        contentDescription = stringResource(R.string.more),
                        modifier = Modifier.size(24.dp)
                    )
                },
                modifier = Modifier.height(56.dp),
                onClick = { showChangeCurrencyModal = true }
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = DividerGray,
                modifier = Modifier.zIndex(1f)
            )
        }
    }

    if (showChangeCurrencyModal) {
        ChangeCurrencyModal(
            onDismissRequest = { showChangeCurrencyModal = false },
            onElementSelected = { currency ->
                currentCurrency = currency
                showChangeCurrencyModal = false
            }
        )
    }
}