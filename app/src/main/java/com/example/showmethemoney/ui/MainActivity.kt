package com.example.showmethemoney.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.showmethemoney.ShowMeTheMoneyApp
import com.example.showmethemoney.ui.components.SplashScreen
import com.example.showmethemoney.ui.screens.MainScreen
import com.example.showmethemoney.ui.theme.BackgroundMainColor
import com.example.showmethemoney.ui.theme.ShowMeTheMoneyTheme
import com.example.showmethemoney.ui.utils.AccountInitializer
import com.example.showmethemoney.ui.utils.UiConfigurator
import javax.inject.Inject

/**
 * Главная Activity приложения, отвечающая за:
 * - Инициализацию и настройку основных компонентов
 * - Управление отображением сплэш-скрина и основного экрана
 * - Загрузку начальных данных пользователя
 *
 * Зависимости:
 * @property viewModelFactory Фабрика для создания [ViewModel]. Внедряется через DI.
 * @property accountInitializer Сервис инициализации аккаунта пользователя. Загружает и кэширует начальные данные.
 * @property uiConfigurator Настройщик параметров UI (ориентация, edge-to-edge и пр.).
 *
 * Жизненный цикл:
 * 1. В [onCreate] происходит:
 *    - Внедрение зависимостей через Dagger/Hilt
 *    - Настройка UI через [uiConfigurator]
 *    - Запуск загрузки аккаунта в фоне [loadInitialAccount]
 *    - Отображение сплэш-скрина с переходом на [MainScreen]
 *
 * Особенности:
 * - Использует [ShowMeTheMoneyTheme] для стилизации
 * - Фон [Surface] переопределён через [BackgroundMainColor]
 * - Короткие операции выполняются в IO-диспетчере
 *
 * @see AccountInitializer для деталей инициализации аккаунта
 * @see MainScreen для структуры основного экрана
 */
class MainActivity : ComponentActivity() {
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var accountInitializer: AccountInitializer
    @Inject lateinit var uiConfigurator: UiConfigurator

    override fun onCreate(savedInstanceState: Bundle?) {
        val repositoryComponent = DaggerRepositoryComponent.factory()
            .create((application as ShowMeTheMoneyApp).coreComponent)

        val viewModelComponent = DaggerViewModelComponent.factory()
            .create(repositoryComponent)

        viewModelComponent.inject(this)


        super.onCreate(savedInstanceState)

        uiConfigurator.configure(this)

        setContent {
            ShowMeTheMoneyTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = BackgroundMainColor),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    SplashScreen(
                        accountInitializer = accountInitializer,
                        onSplashFinished = {
                            MainScreen(viewModelFactory)
                        }
                    )
                }
            }
        }
    }
}