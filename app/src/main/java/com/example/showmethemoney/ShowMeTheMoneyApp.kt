package com.example.showmethemoney

import android.app.Application
import com.example.showmethemoney.di.AppModule
import com.example.showmethemoney.di.NetworkModule

class ShowMeTheMoneyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val appComponent = DaggerAppComponent.builder()
            .appModule(AppModule())
            .networkModule(NetworkModule())
            .build()

        appComponent.inject(this)
    }
}