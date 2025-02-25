package com.jia.opens.golden.scales.towards.jgri6sd


import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.annotation.Keep
import androidx.core.content.ContextCompat
import com.jia.opens.golden.scales.towards.acc.BulletsActivity
import com.jia.opens.golden.scales.towards.jgri6sd.SanShow.KEY_IS_SERVICE
import com.jia.opens.golden.scales.towards.vjire.AllPostFun
import com.jia.opens.golden.scales.towards.zvki6r.fie3h.PngTwoFService
import com.jia.opens.golden.scales.towards.pngstart.MainStart.mainStart
import com.jia.opens.golden.scales.towards.acan.ShowDataTool

@Keep
class SanLiFun : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        SanShow.addActivity(activity)
        ShowDataTool.showLog("FebFiveFffService-launchQTServiceData---4-----${KEY_IS_SERVICE}")
        if (!KEY_IS_SERVICE) {
            ShowDataTool.showLog("FebFiveFffService-launchQTServiceData---5-----${KEY_IS_SERVICE}")
            ContextCompat.startForegroundService(
                mainStart,
                Intent( mainStart, PngTwoFService::class.java)
            )
        }
    }

    override fun onActivityStarted(activity: Activity) {
        if (activity is BulletsActivity) {
            return
        }
        ShowDataTool.showLog("onActivityStarted-name=${activity.javaClass.name}")

        //TODO
        if (activity.javaClass.name.contains("com.kktfs.photo.frame.wallpaper.kktfs.pfw126.MainActivityOld")) {
            ShowDataTool.showLog("onActivityStarted=${activity.javaClass.name}")
            val anTime = SanShow.getInstallTimeDataFun()
            AllPostFun.postPointData(false, "session_front", "time", anTime)
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
        SanShow.removeActivity(activity)
    }

    override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
    }
}
