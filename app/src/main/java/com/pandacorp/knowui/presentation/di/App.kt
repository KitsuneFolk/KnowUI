package com.pandacorp.knowui.presentation.di

import android.app.Application
import com.google.firebase.FirebaseApp
import com.pandacorp.knowui.presentation.di.modules.SingletonModule
import com.pandacorp.knowui.presentation.di.modules.ViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // Throw any uncaught exceptions
        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            throw (throwable)
        }

        FirebaseApp.initializeApp(this)

        startKoin {
            androidContext(this@App)
            modules(listOf(SingletonModule, ViewModelModule))
        }

    }

}