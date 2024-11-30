package com.example.yaksok

import android.app.Application
import com.google.android.libraries.places.api.Places
import com.google.firebase.Firebase
import com.google.firebase.initialize

class YakSokApplication : Application() {
    val appContainer = ServiceLocator()

    override fun onCreate() {
        APPINSTANCE = this
        Firebase.initialize(this)

        Places.initialize(applicationContext, BuildConfig.MAPS_API_KEY)
        super.onCreate()
    }

    companion object {
        private var APPINSTANCE: YakSokApplication? = null
        fun getInstance() = APPINSTANCE!!
    }
}