package com.example.showmethemoney.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.utils.AppColorScheme
import com.example.utils.ThemeManager

@Composable
fun ShowMeTheMoneyTheme(
    darkTheme: Boolean = ThemeManager.isDarkThemeState.value,
    selectedColorScheme: AppColorScheme = ThemeManager.selectedColorSchemeState.value,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> darkColorScheme(
            primary = selectedColorScheme.primary,
            secondary = selectedColorScheme.secondary,
            tertiary = Color(0xFF3700B3),
            background = Color(0xFF121212),
            surface = Color(0xFF1E1E1E),
            onPrimary = Color.Black,
            onSecondary = Color(0xFF1E1E1E),
            onTertiary = Color.White,
            onBackground = Color(0xFFFFFFFF),
            onSurface = Color(0xFFFFFFFF)
        )
        else -> lightColorScheme(
            primary = selectedColorScheme.primary,
            secondary = selectedColorScheme.secondary,
            tertiary = Color(0xFFCAC4D0),
            background = Color(0xFFFEF7FF),
            surface = Color(0xFFFFFBFE),
            onPrimary = Color.White,
            onSecondary = Color(0xFFF3EDF7),
            onTertiary = Color(0xFF49454F),
            onBackground = Color(0xFF1C1B1F),
            onSurface = Color(0xFF1C1B1F)
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MoneyTypography,
        content = content
    )
}