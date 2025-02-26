package com.healthybody.happyeveryday.xxs.util

import android.app.Application
import com.jia.opens.golden.scales.towards.pngstart.startApp
import com.tencent.mmkv.MMKV

class App: Application() {

    companion object {
        lateinit var app:App
    }
    override fun onCreate() {
        super.onCreate()
        app = this
        MMKV.initialize(this)
        Local.mmkv = MMKV.defaultMMKV()
        startApp.init(this, false)
    }
}