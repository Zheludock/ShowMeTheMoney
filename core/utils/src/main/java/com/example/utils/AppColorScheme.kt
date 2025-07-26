package com.example.utils

import androidx.compose.ui.graphics.Color


enum class AppColorScheme(val primary: Color, val secondary: Color) {
    BLUE(Color(0xFF2196F3), Color(0xFF64B5F6)),
    GREEN(Color(0xFF2AE881), Color(0xFFD4FAE6)),
    ORANGE(Color(0xFFFF9800), Color(0xFFFFB74D));

    companion object {
        val DEFAULT = GREEN
    }
}