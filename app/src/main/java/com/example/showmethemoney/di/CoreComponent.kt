package com.example.showmethemoney.di

import android.app.Application
import android.content.Context
import com.example.data.di.NetworkModule
import com.example.data.di.SafeApiModule
import com.example.data.safecaller.ApiCallHelper
import dagger.BindsInstance
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    NetworkModule::class,
    SafeApiModule::class,
    NetworkMonitorModule::class
])
interface CoreComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application,
            appModule: AppModule
        ): CoreComponent
    }
    fun application(): Application
    fun context(): Context
    fun retrofit(): Retrofit
    fun apiCallHelper(): ApiCallHelper
}