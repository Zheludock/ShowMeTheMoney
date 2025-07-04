package com.example.showmethemoney

import android.app.Application
import com.example.showmethemoney.di.AppComponent
import com.example.showmethemoney.di.AppModule
import com.example.showmethemoney.di.DaggerAppComponent
/**
 * Главный класс приложения, инициализирующий [appComponent] для внедрения зависимостей.
 */
class ShowMeTheMoneyApp : Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.factory()
            .create(application = this, appModule = AppModule())
    }
}