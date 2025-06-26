package com.example.showmethemoney.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.example.domain.repository.NetworkMonitor
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import javax.inject.Inject

/**
 * Реализация [NetworkMonitor] для Android, отслеживающая состояние интернет-соединения.
 *
 * Использует [ConnectivityManager] для мониторинга изменений состояния сети и предоставляет
 * поток [isOnline] с текущим состоянием подключения (true/false).
 *
 * @param context Контекст приложения для доступа к системным сервисам
 *
 * @see NetworkMonitor Интерфейс монитора сети
 * @see ConnectivityManager Системный сервис для работы с сетевыми подключениями
 */
class AndroidNetworkMonitor @Inject constructor(
    context: Context
) : NetworkMonitor  {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /**
     * Поток (Flow), содержащий текущее состояние интернет-соединения.
     * Возвращает true, если устройство подключено к интернету, false - если нет.
     *
     * При изменении состояния сети поток автоматически обновляет значение.
     */
    override val isOnline: Flow<Boolean> = callbackFlow {
        // Инициализация начального состояния
        val currentState = connectivityManager.activeNetwork?.let { network ->
            connectivityManager.getNetworkCapabilities(network)
                ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        } ?: false
        trySend(currentState).isSuccess
        // Callback для отслеживания изменений состояния сети
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true).isSuccess
            }

            override fun onLost(network: Network) {
                trySend(false).isSuccess
            }
        }
        // Регистрация callback
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)
        // Отмена регистрации при закрытии потока
        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.debounce(500)
}