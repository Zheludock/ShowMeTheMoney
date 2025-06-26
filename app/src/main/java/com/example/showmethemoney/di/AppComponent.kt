package com.example.showmethemoney.di

import android.app.Application
import com.example.data.NetworkModule
import com.example.data.di.RepositoryModule
import com.example.showmethemoney.ui.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    NetworkModule::class,
    RepositoryModule::class,
    NetworkMonitorModule::class,
    ViewModelModule::class,
    ViewModelBindingModule::class
])
interface AppComponent  {
    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application,
            appModule: AppModule
        ): AppComponent
    }
}