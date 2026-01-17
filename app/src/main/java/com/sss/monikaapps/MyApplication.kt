package com.sss.monikaapps

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.sss.monikaapps.di.AppModule.apiModule
import com.sss.monikaapps.di.AppModule.databaseModule
import com.sss.monikaapps.di.AppModule.repositoryModule
import com.sss.monikaapps.di.AppModule.viewModelModule
import com.sss.monikaapps.common.manager.ServerManager
import com.sss.monikaapps.common.manager.SessionManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        ServerManager.getInstance().init(this)
        SessionManager.getInstance().init(this)
        startKoin {
            androidContext(this@MyApplication)
            modules(listOf(apiModule, repositoryModule, databaseModule, viewModelModule))
        }
    }
}