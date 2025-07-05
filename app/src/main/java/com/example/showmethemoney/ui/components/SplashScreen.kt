package com.example.showmethemoney.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.showmethemoney.R
import com.example.showmethemoney.ui.utils.AccountInitializer
import kotlinx.coroutines.delay

/**
 * Компонуемый экран заставки (Splash Screen) с анимацией.
 *
 * Выполняет:
 * 1. Инициализацию аккаунта через AccountInitializer
 * 2. Отображение анимированного логотипа/изображения
 * 3. Автоматический переход после завершения инициализации и минимальной задержки
 *
 * @param accountInitializer Сервис инициализации аккаунта, должен реализовывать метод initialize()
 * @param onSplashFinished Callback-функция, вызываемая после завершения показа заставки.
 *                         Должна содержать логику перехода на следующий экран.
 */
@Composable
fun SplashScreen(
    accountInitializer: AccountInitializer,
    onSplashFinished: @Composable () -> Unit
) {
    var showContent by remember { mutableStateOf(false) }
    var initializationComplete by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        accountInitializer.initialize()
        initializationComplete = true
    }

    LaunchedEffect(Unit) {
        delay(2000)
        while (!initializationComplete) {
            delay(100)
        }
        showContent = true
    }

    AnimatedVisibility(
        visible = !showContent,
        enter = fadeIn(animationSpec = tween(durationMillis = 700)),
        exit = fadeOut(animationSpec = tween(durationMillis = 300))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.show),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }
    }

    if (showContent) {
        onSplashFinished()
    }
}