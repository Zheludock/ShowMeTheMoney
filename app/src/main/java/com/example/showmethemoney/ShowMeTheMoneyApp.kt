package com.example.showmethemoney

import android.app.Application
import com.example.showmethemoney.di.AppModule
import com.example.showmethemoney.di.CoreComponent
import com.example.showmethemoney.di.DaggerCoreComponent

/**
 * Главный класс приложения, инициализирующий [appComponent] для внедрения зависимостей.
 */
class ShowMeTheMoneyApp : Application() {
    lateinit var coreComponent: CoreComponent
        private set

    override fun onCreate() {
        super.onCreate()

        coreComponent = DaggerCoreComponent.factory()
            .create(this, AppModule())
    }
}