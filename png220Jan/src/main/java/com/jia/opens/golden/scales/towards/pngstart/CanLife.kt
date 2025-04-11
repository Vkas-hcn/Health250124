package com.jia.opens.golden.scales.towards.pngstart


import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.annotation.Keep
import androidx.core.content.ContextCompat
import com.jia.opens.golden.scales.towards.acc.PjActivity
import com.jia.opens.golden.scales.towards.vjire.PngCanGo.KEY_IS_SERVICE
import com.jia.opens.golden.scales.towards.vjire.TtPoint
import com.jia.opens.golden.scales.towards.zvki6r.fie3h.PngTwoFService
import com.jia.opens.golden.scales.towards.pngstart.startApp.mainStart
import com.jia.opens.golden.scales.towards.acan.ShowDataTool
import com.jia.opens.golden.scales.towards.vjire.PngCanGo

@Keep
class CanLife : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        PngCanGo.addActivity(activity)
        if (!KEY_IS_SERVICE && activity.javaClass.name !="com.healthybody.happyeveryday.xxs.MainTwoActivity") {
            PngCanGo.startService(activity)
        }
    }

    override fun onActivityStarted(activity: Activity) {
        if (activity is PjActivity) {
            return
        }
        if (activity.javaClass.name.contains("com.healthybody.happyeveryday.xxs.MainTwoActivity")) {
            ShowDataTool.showLog("onActivityStarted=${activity.javaClass.name}")
            val anTime = PngCanGo.getInstallTimeDataFun()
            TtPoint.postPointData(false, "session_front", "time", anTime)
        }
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        PngCanGo.removeActivity(activity)
    }

    override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
    }
}
