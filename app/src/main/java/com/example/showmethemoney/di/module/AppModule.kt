package com.example.showmethemoney.di.module

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.data.sync.SyncWorker
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
    fun provideContext(application: Application): Context = application.applicationContext

    /**
     * Предоставляет [ConnectivityManager] для работы с сетевым состоянием.
     * @param context Контекст для доступа к системному сервису.
     */
    @Provides
    @Singleton
    fun provideConnectivityManager(
        context: Context
    ): ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Singleton
    fun provideWorkerFactory(
        syncWorkerFactory: SyncWorker.SyncWorkerFactory
    ): WorkerFactory {
        return object : WorkerFactory() {
            override fun createWorker(
                appContext: Context,
                workerClassName: String,
                workerParameters: WorkerParameters
            ): ListenableWorker? {
                return when (workerClassName) {
                    SyncWorker::class.java.name ->
                        syncWorkerFactory.create(appContext, workerParameters)
                    else -> null
                }
            }
        }
    }
}