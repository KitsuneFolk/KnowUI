package com.pandacorp.knowui.presentation.di

import android.app.Application
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    private var listOfModules = listOf(koinModule)

    override fun onCreate() {
        super.onCreate()
        // Throw any uncaught exceptions
        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            throw (throwable)
        }

        FirebaseApp.initializeApp(this)

        startKoin {
            androidContext(this@App)
            modules(listOfModules)
        }

    }

}