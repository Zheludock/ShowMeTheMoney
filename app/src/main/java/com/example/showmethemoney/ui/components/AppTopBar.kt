package com.example.showmethemoney.ui.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.showmethemoney.R
import com.example.showmethemoney.ui.theme.IconsGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(title: String,
              navController: NavController,
              onActionIconClick: (() -> Unit)? = null) {
    val actionIcon = when (title) {
        "Расходы сегодня", "Доходы сегодня" -> R.drawable.ic_history
        "Мой счет" -> R.drawable.ic_edit
        "Моя история" -> R.drawable.ic_history_calendar
        else -> null
    }
    val navigationIcon = when(title) {
        "Моя история" -> R.drawable.ic_back
        else -> null
    }
    CenterAlignedTopAppBar(
        title = { Text(text = title) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = IconsGreen
        ),
        actions = {
            actionIcon?.let { iconRes ->
                IconButton(onClick = { onActionIconClick?.invoke() }) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = "какое-то описание"
                    )
                }
            }
        },
        navigationIcon = {
            navigationIcon?.let { iconRes ->
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = "Навигационная иконка"
                    )
                }
            }
        },
    )
}