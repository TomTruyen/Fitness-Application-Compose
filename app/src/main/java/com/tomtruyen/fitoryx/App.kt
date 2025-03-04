package com.tomtruyen.fitoryx

import android.app.Application
import com.tomtruyen.data.di.databaseModule
import com.tomtruyen.data.di.repositoryModule
import com.tomtruyen.fitoryx.di.appModule
import com.tomtruyen.fitoryx.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.unloadKoinModules

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        setupKoin()
    }

    private fun setupKoin() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(koinModules)
        }
    }

    companion object {
        private val koinModules = listOf(
            appModule,
            viewModelModule,
            databaseModule,
            repositoryModule
        )

        fun reloadKoinModules() {
            unloadKoinModules(koinModules)
            loadKoinModules(koinModules)
        }
    }
}