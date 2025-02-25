package com.jia.opens.golden.scales.towards.zvki6r.fie3h

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import android.widget.RemoteViews
import com.jia.opens.golden.scales.towards.R
import com.jia.opens.golden.scales.towards.jgri6sd.SanShow.KEY_IS_SERVICE
import com.jia.opens.golden.scales.towards.acan.ShowDataTool

class PngTwoFService : Service() {
    @SuppressLint("ForegroundServiceType", "RemoteViewLayout")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        ShowDataTool.showLog("FebFiveFffService onStartCommand-1=${KEY_IS_SERVICE}")
        if (!KEY_IS_SERVICE) {
            KEY_IS_SERVICE =true
            val channel = NotificationChannel("sancango", "sancango", NotificationManager.IMPORTANCE_MIN)
            channel.setSound(null, null)
            channel.enableLights(false)
            channel.enableVibration(false)
            (application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).run {
                if (getNotificationChannel(channel.toString()) == null) createNotificationChannel(channel)
            }
            runCatching {
                startForeground(
                    7788,
                    NotificationCompat.Builder(this, "sancango").setSmallIcon(R.drawable.show_can_t)
                        .setContentText("")
                        .setContentTitle("")
                        .setOngoing(true)
                        .setCustomContentView(RemoteViews(packageName, R.layout.layout_no))
                        .build()
                )
            }
            ShowDataTool.showLog("FebFiveFffService onStartCommand-2=${KEY_IS_SERVICE}")
        }
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


}
