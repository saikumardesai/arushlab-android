package com.arushlab.android

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ArushLabApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
