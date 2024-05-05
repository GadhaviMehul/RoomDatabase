package com.example.roomdatabase42024

import android.app.Application
import android.content.Context

class App : Application() {
    companion object {
        private var INSTANCES: App? = null

        fun getApplicationContext(): Context {
            return INSTANCES!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCES = this
    }
}