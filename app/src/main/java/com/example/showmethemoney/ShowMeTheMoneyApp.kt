package com.example.showmethemoney

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkerFactory
import com.example.data.sync.SyncWorker
import com.example.showmethemoney.di.component.CoreComponent
import com.example.showmethemoney.di.component.DaggerCoreComponent
import com.example.showmethemoney.di.component.DaggerRepositoryComponent
import com.example.showmethemoney.di.component.RepositoryComponent
import javax.inject.Inject

/**
 * Главный класс приложения, инициализирующий [coreComponent] для внедрения зависимостей.
 */
class ShowMeTheMoneyApp: Application(), Configuration.Provider {
    lateinit var coreComponent: CoreComponent
        private set

    @Inject
    lateinit var workerFactory: WorkerFactory

    lateinit var repositoryComponent: RepositoryComponent
        private set

    override fun onCreate() {
        super.onCreate()

        coreComponent = DaggerCoreComponent.factory().create(this)

        repositoryComponent = DaggerRepositoryComponent.factory().create(coreComponent)

        setupSyncWorker()
    }

    private fun setupSyncWorker() {
        SyncWorker.setup(this)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}