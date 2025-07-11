package com.example.showmethemoney

import android.app.Application
import android.os.StrictMode
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

        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()       // Чтение с диска в основном потоке
                .detectDiskWrites()     // Запись на диск в основном потоке
                .detectNetwork()        // Сетевые операции в основном потоке
                .penaltyLog()           // Логировать нарушения
                .build()
        )

        // Настройка проверок для утечек памяти (VmPolicy)
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()    // Утечки SQLite
                .detectLeakedClosableObjects()   // Утечки Closeable (файлы, сокеты)
                .penaltyLog()                    // Логировать нарушения
                .build()
        )
    }
}