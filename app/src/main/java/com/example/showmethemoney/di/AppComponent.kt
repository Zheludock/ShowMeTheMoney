package com.example.showmethemoney.di

import android.app.Application
import com.example.data.di.NetworkModule
import com.example.data.di.RepositoryModule
import com.example.data.di.SafeApiModule
import com.example.showmethemoney.ui.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * Главный компонент приложения, обеспечивающий dependency injection через Dagger 2.
 *
 * Включает модули:
 * - [AppModule] — общие зависимости приложения.
 * - [NetworkModule] — настройки сети (API, клиенты).
 * - [RepositoryModule] — репозитории для работы с данными.
 * - [NetworkMonitorModule] — мониторинг состояния сети.
 * - [ViewModelModule] — фабрики ViewModel.
 * - [ViewModelBindingModule] — привязки ViewModel к Android-компонентам.
 *
 * @see AppComponent.Factory для создания экземпляра с кастомными параметрами.
 */

@Singleton
@Component(modules = [
    AppModule::class,
    NetworkModule::class,
    RepositoryModule::class,
    NetworkMonitorModule::class,
    ViewModelModule::class,
    ViewModelBindingModule::class,
    SafeApiModule::class
])
interface AppComponent  {
    /**
     * Инжектирует зависимости в [MainActivity].
     */
    fun inject(activity: MainActivity)
    /**
     * Фабрика для создания [AppComponent] с возможностью передачи кастомных зависимостей.
     */
    @Component.Factory
    interface Factory {
        /**
         * @param application Экземпляр [Application] (биндится как singleton).
         * @param appModule Модуль с общими зависимостями приложения.
         */
        fun create(
            @BindsInstance application: Application,
            appModule: AppModule
        ): AppComponent
    }
}