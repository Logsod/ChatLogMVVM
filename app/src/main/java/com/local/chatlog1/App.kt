package com.local.chatlog1

import android.app.Application
import com.local.chatlog1.di.component.AppComponent
import com.local.chatlog1.di.component.DaggerAppComponent

class App : Application() {
    lateinit var appComponent : AppComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
    }
}