package com.abhishekpathak.posts.core.application

import android.app.Application
import com.facebook.stetho.Stetho
import com.abhishekpathak.posts.core.BuildConfig
import com.abhishekpathak.posts.core.di.AppModule
import com.abhishekpathak.posts.core.di.CoreComponent
import com.abhishekpathak.posts.core.di.DaggerCoreComponent
import com.abhishekpathak.posts.core.networking.synk.Synk

open class CoreApp : Application() {

    companion object {
        lateinit var coreComponent: CoreComponent
    }

    override fun onCreate() {
        super.onCreate()
        initSynk()
        initDI()
        initStetho()
    }

    private fun initSynk() {
        Synk.init(this)
    }

    private fun initStetho() {
        if (BuildConfig.DEBUG)
            Stetho.initializeWithDefaults(this)
    }

    private fun initDI() {
        coreComponent = DaggerCoreComponent.builder().appModule(AppModule(this)).build()
    }
}