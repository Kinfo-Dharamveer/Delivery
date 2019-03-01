package com.jointdelivery

import android.app.Application
import com.jointdelivery.di.AppComponent
import com.jointdelivery.di.ApplicationModule
import com.jointdelivery.di.DaggerAppComponent

open class MyApplication: Application() {
    private var sAppComponent: AppComponent? = null

    fun getAppComponent(): AppComponent? {
        if (sAppComponent == null) {
            buildAppComponent()
        }
        return sAppComponent
    }

    override fun onCreate() {
        super.onCreate()
        buildAppComponent()
    }

    private fun buildAppComponent() {
        sAppComponent = DaggerAppComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }
}
