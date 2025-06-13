package com.example.showmethemoney.ui.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.showmethemoney.R
import com.example.showmethemoney.ui.theme.IconsGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(title: String) {
    val actionIcon = when (title) {
        "Расходы сегодня", "Доходы сегодня" -> R.drawable.ic_history
        "Мой счет" -> R.drawable.ic_edit
        else -> null // Иконка по умолчанию
    }
    CenterAlignedTopAppBar(
        title = { Text(text = title) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = IconsGreen
        ),
        actions = {
            actionIcon?.let { iconRes ->
                IconButton(onClick = { /* Обработка нажатия */ }) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = "какое-то описание"
                    )
                }
            }
        }
    )
}