package com.example.yaksok

import android.app.Application

class YakSokApplication : Application() {
    val appContainer = ServiceLocator()

    override fun onCreate() {
        APPINSTANCE = this
        //TODO 파이어베이스 초기화작업

        super.onCreate()
    }

    companion object {
        private var APPINSTANCE: YakSokApplication? = null
        fun getInstance() = APPINSTANCE!!
    }
}