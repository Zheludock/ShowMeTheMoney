package com.example.showmethemoney

import android.app.Application
import com.example.showmethemoney.di.component.CoreComponent
import com.example.showmethemoney.di.component.DaggerCoreComponent
import com.example.showmethemoney.di.component.DaggerRepositoryComponent
import com.example.showmethemoney.di.component.RepositoryComponent

/**
 * Главный класс приложения, инициализирующий [coreComponent] для внедрения зависимостей.
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