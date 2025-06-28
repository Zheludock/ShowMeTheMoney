package com.example.showmethemoney.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Dagger-модуль для предоставления зависимостей уровня приложения.
 *
 * Предоставляет:
 * - Контекст приложения ([Context]).
 * - [ConnectivityManager] для мониторинга состояния сети.
 */
@Module
class AppModule {
    /**
     * Предоставляет контекст приложения ([Context]) как singleton.
     * Альтернатива прямому использованию [provideApplication], если нужен именно контекст.
     */
    @Provides
    @Singleton
    fun provideApplicationContext(app: Application): Context = app

    /**
     * Предоставляет [ConnectivityManager] для работы с сетевым состоянием.
     * @param context Контекст для доступа к системному сервису.
     */
    @Provides
    @Singleton
    fun provideConnectivityManager(
        context: Context
    ): ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}