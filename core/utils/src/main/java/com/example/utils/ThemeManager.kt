package com.example.utils

import android.content.Context
import androidx.compose.runtime.mutableStateOf

object ThemeManager {
    private const val PREFS_NAME = "theme_prefs"
    private const val KEY_DARK_THEME = "dark_theme_enabled"
    private const val KEY_COLOR_SCHEME = "color_scheme"

    // Состояния для мгновенного обновления UI
    var isDarkThemeState = mutableStateOf(false)
        private set

    var selectedColorSchemeState = mutableStateOf(AppColorScheme.DEFAULT)
        private set

    fun initialize(context: Context) {
        isDarkThemeState.value = isDarkThemeEnabled(context)
        selectedColorSchemeState.value = getSelectedColorScheme(context)
    }

    fun isDarkThemeEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_DARK_THEME, false)
    }

    fun setDarkThemeEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_DARK_THEME, enabled).apply()
        isDarkThemeState.value = enabled
    }

    fun getSelectedColorScheme(context: Context): AppColorScheme {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val schemeName = prefs.getString(KEY_COLOR_SCHEME, AppColorScheme.DEFAULT.name)
        return try {
            AppColorScheme.valueOf(schemeName ?: AppColorScheme.DEFAULT.name)
        } catch (e: IllegalArgumentException) {
            AppColorScheme.DEFAULT
        }
    }

    fun setSelectedColorScheme(context: Context, scheme: AppColorScheme) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_COLOR_SCHEME, scheme.name).apply()
        selectedColorSchemeState.value = scheme
    }
}