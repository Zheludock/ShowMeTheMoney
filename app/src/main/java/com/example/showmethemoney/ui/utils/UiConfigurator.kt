package com.example.showmethemoney.ui.utils

import android.content.pm.ActivityInfo
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import javax.inject.Inject

/**
 * Конфигуратор базовых параметров UI для Activity.
 * Реализует настройки, которые должны быть применены ко всем экранам приложения.
 *
 * - Фиксирует портретную ориентацию экрана
 * - Активирует edge-to-edge отображение (заход контента под системные панели)
 *
 * ## Пример использования:
 * class MainActivity : ComponentActivity() {
 *     @Inject lateinit var uiConfigurator: UiConfigurator
 *
 *     override fun onCreate(savedInstanceState: Bundle?) {
 *         uiConfigurator.configure(this)
 *         // ... остальная инициализация
 *     }
 * }
 * @see ComponentActivity.enableEdgeToEdge
 * @see ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
 */
class UiConfigurator @Inject constructor(){
    fun configure(activity: ComponentActivity) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        activity.enableEdgeToEdge()
    }
}