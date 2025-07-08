package com.example.showmethemoney.ui.screens.category

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.domain.response.ApiResult
import com.example.showmethemoney.R
import com.example.showmethemoney.ui.components.AppTopBar
import com.example.showmethemoney.ui.components.ErrorView
import com.example.showmethemoney.ui.components.LoadingIndicator
import com.example.showmethemoney.ui.screens.TopBarState

/**
 * Экран отображения списка категорий с обработкой состояний загрузки.
 *
 * @param viewModelFactory Фабрика для создания CategoryViewModel
 *
 * Состояния:
 * - Loading: Показывает индикатор загрузки
 * - Success: Отображает список категорий
 * - Error: Показывает ошибку с возможностью повтора
 */
@Composable
fun CategoryScreen(
    viewModelFactory: ViewModelProvider.Factory,
    updateTopBar: (TopBarState) -> Unit
) {

    val viewModel: CategoryViewModel = viewModel(factory = viewModelFactory)

    LaunchedEffect(Unit) {
        updateTopBar(
            TopBarState(
                title = "Мои статьи"
            )
        )
    }

    LaunchedEffect(Unit) {
        viewModel.loadCategories()
    }
    val categoriesState by viewModel.categories.collectAsState()

    when (val state = categoriesState) {
        is ApiResult.Loading -> LoadingIndicator()
        is ApiResult.Success -> CategoryList(state.data)
        is ApiResult.Error -> ErrorView(state.error) {
            viewModel.loadCategories()
        }
    }
}




