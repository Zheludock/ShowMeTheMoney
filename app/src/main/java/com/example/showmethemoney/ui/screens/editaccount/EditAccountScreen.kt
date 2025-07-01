package com.example.showmethemoney.ui.screens.editaccount

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.domain.response.ApiResult
import com.example.showmethemoney.R
import com.example.showmethemoney.navigation.Screen
import com.example.showmethemoney.ui.components.AppTopBar
import com.example.showmethemoney.ui.components.UniversalListItem
import com.example.showmethemoney.ui.screens.account.AccountDetailsItem
import com.example.showmethemoney.ui.theme.Indicator
import com.example.showmethemoney.ui.utils.AccountManager
import com.example.showmethemoney.ui.utils.StringFormatter

@Composable
fun EditAccountScreen(
    viewModelFactory: ViewModelProvider.Factory,
    navController: NavController
) {
    val viewModel: EditAccountViewModel = viewModel(factory = viewModelFactory)

    var showChangeCurrencyModal by remember { mutableStateOf(false) }

    val accountDetailsState by viewModel.accountDetails.collectAsState()

    var accountName by remember { mutableStateOf("") }
    var accountBalance by remember { mutableStateOf("") }
    var currentCurrency by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadAccountDetails()
    }

    LaunchedEffect(accountDetailsState) {
        if (accountDetailsState is ApiResult.Success) {
            val data = (accountDetailsState as ApiResult.Success<AccountDetailsItem>).data
            accountName = data.name
            accountBalance = data.balance
            currentCurrency = data.currency
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            AppTopBar(
                title = stringResource(R.string.edit_account),
                onActionIconClick = {
                    viewModel.updateAccount(
                        name = accountName,
                        currency = currentCurrency,
                        balance = accountBalance
                    )
                    AccountManager.selectedAccountName = accountName
                    navController.navigate(Screen.Account.route)
                },
                navController = navController
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            LazyColumn {
                item {
                    TextField(
                        value = accountName,
                        onValueChange = { accountName = it },
                        label = { Text(stringResource(R.string.account_name)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(56.dp)
                    )
                }
                item {
                    TextField(
                        value = accountBalance,
                        onValueChange = { accountBalance = it },
                        label = { Text(stringResource(R.string.balance)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(56.dp)
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
                        currentCurrency = currency
                        showChangeCurrencyModal = false
                    }
                )
            }
        }
    }
}