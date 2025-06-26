package com.example.showmethemoney.ui.screens.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.domain.ApiResult
import com.example.showmethemoney.ui.components.ErrorView
import com.example.showmethemoney.ui.components.LoadingIndicator
import com.example.showmethemoney.ui.components.AccountContent

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
