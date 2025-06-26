package com.example.showmethemoney.ui.screens.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.domain.ApiResult
import com.example.domain.model.AccountHistoryDomain
import com.example.showmethemoney.R
import com.example.showmethemoney.ui.components.ErrorView
import com.example.showmethemoney.ui.components.LoadingIndicator
import com.example.showmethemoney.ui.components.UniversalListItem
import com.example.showmethemoney.ui.theme.Indicator

@Composable
fun AccountScreen(viewModelFactory: ViewModelProvider.Factory) {
    val viewModel: AccountViewModel = viewModel(factory = viewModelFactory)

    LaunchedEffect(Unit) {
        viewModel.loadAccountHistory()
    }
    val accountHistoryState by viewModel.accountHistory.collectAsState()

    when (val state = accountHistoryState) {
        is ApiResult.Loading -> LoadingIndicator()
        is ApiResult.Success -> AccountContent(state.data)
        is ApiResult.Error -> ErrorView(state.error) {
            viewModel.loadAccountHistory()
        }
    }
}

@Composable
private fun AccountContent(history: AccountHistoryDomain) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            UniversalListItem(
                lead = "💰",
                content = "Баланс" to null,
                trail = "${history.currentBalance} ${history.currency}" to null,
                modifier = Modifier
                    .background(Indicator)
                    .height(56.dp)
            )
        }
        item {
            UniversalListItem(
                content = "Валюта" to null,
                trail = history.currency to {
                    IconButton(
                        onClick = { /* TODO */ },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_more_vert),
                            contentDescription = "Подробнее",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                modifier = Modifier
                    .background(Indicator)
                    .height(56.dp)
            )
        }
    }
}
