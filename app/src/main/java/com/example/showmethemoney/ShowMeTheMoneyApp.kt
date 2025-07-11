package com.example.showmethemoney

import android.app.Application
import com.example.showmethemoney.di.CoreComponent
import com.example.showmethemoney.di.DaggerCoreComponent
import com.example.showmethemoney.di.DaggerRepositoryComponent
import com.example.showmethemoney.di.RepositoryComponent

/**
 * Главный класс приложения, инициализирующий [appComponent] для внедрения зависимостей.
 */
class ShowMeTheMoneyApp : Application() {
    lateinit var coreComponent: CoreComponent
        private set

    lateinit var repositoryComponent: RepositoryComponent
        private set

    override fun onCreate() {
        super.onCreate()

        coreComponent = DaggerCoreComponent.factory().create(this)

        repositoryComponent = DaggerRepositoryComponent.factory().create(coreComponent)
    }
}