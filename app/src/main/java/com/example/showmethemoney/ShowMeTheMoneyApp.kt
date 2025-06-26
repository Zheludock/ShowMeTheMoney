package com.example.showmethemoney

import android.app.Application
import com.example.showmethemoney.di.DaggerAppComponent

class ShowMeTheMoneyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val appComponent = DaggerAppComponent.create()

    }
}