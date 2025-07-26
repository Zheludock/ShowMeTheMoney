package com.example.settings.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.ui.UniversalListItem
import com.example.utils.TopBarState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AboutScreen(updateTopBar: (TopBarState) -> Unit,) {
    LaunchedEffect(Unit) {
        updateTopBar(
            TopBarState(
                title = "О приложении"
            )
        )
    }
    val context = LocalContext.current
    val packageInfo = remember {
        try {
            context.packageManager.getPackageInfo(context.packageName, 0)
        } catch (e: Exception) {
            null
        }
    }
    val lastUpdateTime = remember {
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                .format(Date(packageInfo.lastUpdateTime))
        } catch (e: Exception) {
            "Неизвестно"
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        UniversalListItem(content = "Версия приложения" to null,
            trail = "${packageInfo?.versionName}" to null)
        UniversalListItem(content = "Дата сборки" to null,
            trail = "${lastUpdateTime}" to null)
    }
}