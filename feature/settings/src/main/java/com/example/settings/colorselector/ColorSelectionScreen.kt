package com.example.settings.colorselector

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.utils.AppColorScheme
import com.example.utils.ThemeManager
import com.example.utils.TopBarState

@Composable
fun ColorSelectionScreen(
    updateTopBar: (TopBarState) -> Unit,
) {

    LaunchedEffect(Unit) {
        updateTopBar(
            TopBarState(
                title = "Выбор основного цвета"
            )
        )
    }
    val context = LocalContext.current
    val selectedColorScheme = remember { mutableStateOf(ThemeManager.getSelectedColorScheme(context)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AppColorScheme.values().forEach { scheme ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable{ ThemeManager.setSelectedColorScheme(context, scheme) }
                    .padding(vertical = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(scheme.primary, CircleShape)
                        .border(
                            width = 2.dp,
                            color = if (scheme == selectedColorScheme.value) MaterialTheme.colorScheme.primary
                            else Color.Transparent,
                            shape = CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = scheme.name,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}