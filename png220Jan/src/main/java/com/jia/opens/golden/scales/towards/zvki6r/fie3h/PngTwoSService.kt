package com.jia.opens.golden.scales.towards.zvki6r.fie3h

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.annotation.Keep


@Keep
class PngTwoSService:Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }
}