package com.example.showmethemoney

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.showmethemoney.ui.screens.MainScreen
import com.example.showmethemoney.ui.screens.MainViewModel
import com.example.showmethemoney.ui.components.SplashScreen
import com.example.showmethemoney.ui.theme.BackgroundMainColor
import com.example.showmethemoney.ui.theme.ShowMeTheMoneyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShowMeTheMoneyTheme {
                var showSplash by remember { mutableStateOf(true) }
                Surface(
                    modifier = Modifier.fillMaxSize()
                        .background(color = BackgroundMainColor),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val viewModel: MainViewModel = viewModel()
                    if (showSplash) {
                        SplashScreen { showSplash = false }
                    } else {
                        MainScreen(viewModel)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val viewModel: MainViewModel = viewModel()
    MainScreen(viewModel)
}