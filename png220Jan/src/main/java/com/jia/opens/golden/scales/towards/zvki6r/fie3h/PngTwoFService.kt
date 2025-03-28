package com.jia.opens.golden.scales.towards.zvki6r.fie3h

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import android.widget.RemoteViews
import com.jia.opens.golden.scales.towards.R
import com.jia.opens.golden.scales.towards.vjire.PngCanGo.KEY_IS_SERVICE
import com.jia.opens.golden.scales.towards.acan.ShowDataTool

class PngTwoFService : Service() {
    @SuppressLint("ForegroundServiceType", "RemoteViewLayout")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        ShowDataTool.showLog("FebFiveFffService onStartCommand-1=${KEY_IS_SERVICE}")
        if (!KEY_IS_SERVICE) {
            KEY_IS_SERVICE = true
            val channel =
                NotificationChannel("pngJia", "pngJia", NotificationManager.IMPORTANCE_MIN)
            channel.setSound(null, null)
            channel.enableLights(false)
            channel.enableVibration(false)
            (application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).run {
                if (getNotificationChannel(channel.toString()) == null) createNotificationChannel(
                    channel
                )
            }
            runCatching {
                //判断Android 14
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    startForeground(
                        5633,
                        NotificationCompat.Builder(this, "pngJia").setSmallIcon(R.drawable.ces_show)
                            .setContentText("")
                            .setContentTitle("")
                            .setOngoing(true)
                            .setCustomContentView(RemoteViews(packageName, R.layout.layout_no))
                            .build(),
                        FOREGROUND_SERVICE_TYPE_SPECIAL_USE
                    );
                } else {
                    startForeground(
                        5633,
                        NotificationCompat.Builder(this, "pngJia").setSmallIcon(R.drawable.ces_show)
                            .setContentText("")
                            .setContentTitle("")
                            .setOngoing(true)
                            .setCustomContentView(RemoteViews(packageName, R.layout.layout_no))
                            .build()
                    )
                }


            }
            ShowDataTool.showLog("FebFiveFffService onStartCommand-2=${KEY_IS_SERVICE}")
        }
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


}
