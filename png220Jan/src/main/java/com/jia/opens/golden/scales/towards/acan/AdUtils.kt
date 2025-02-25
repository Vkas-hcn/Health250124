package com.jia.opens.golden.scales.towards.acan

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.os.PowerManager
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.jia.opens.golden.scales.towards.jgri6sd.SanZong
import com.jia.opens.golden.scales.towards.vjire.AllPostFun
import com.jia.opens.golden.scales.towards.vjire.SanPutData
import com.jia.opens.golden.scales.towards.pngstart.MainStart
import com.jia.opens.golden.scales.towards.pngstart.MainStart.mainStart
import com.jia.opens.golden.scales.towards.pngstart.MainStart.mustXS
import com.jia.opens.golden.scales.towards.pngstart.MainStart.splitString
import com.jia.opens.golden.scales.towards.zsdk5f.ZJiaPng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

object AdUtils {
    var adShowTime: Long = 0
    var showAdTime: Long = 0
    fun sessionUp() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                AllPostFun.postPointData(false, "session_up")
                delay(1000 * 60 * 15)
            }
        }
    }

    fun noShowICCC() {
        CoroutineScope(Dispatchers.Main).launch {
            val isaData = ShowDataTool.getAdminData()
            if (isaData == null || isaData.appConfig.userTier!=1) {
                ShowDataTool.showLog("不是A方案显示图标")
//                ZJiaPng.jiaPng( 24)
            }
        }
    }

    fun initAppsFlyer() {
        ShowDataTool.showLog("AppsFlyer-id: $${SanZong.getAppsflyId()}")
        AppsFlyerLib.getInstance()
            .init(SanZong.getAppsflyId(), object : AppsFlyerConversionListener {
                override fun onConversionDataSuccess(conversionDataMap: MutableMap<String, Any>?) {
                    //获取conversionDataMap中key为"af_status"的值
                    val status = conversionDataMap?.get("af_status") as String?
                    ShowDataTool.showLog("AppsFlyer: $status")
                    SanPutData.pointInstallAf(status.toString())
                    //打印conversionDataMap值
                    conversionDataMap?.forEach { (key, value) ->
                        ShowDataTool.showLog("AppsFlyer-all: key=$key: value=$value")
                    }
                }

                override fun onConversionDataFail(p0: String?) {
                    ShowDataTool.showLog("AppsFlyer: onConversionDataFail$p0")
                }

                override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
                    ShowDataTool.showLog("AppsFlyer: onAppOpenAttribution$p0")
                }

                override fun onAttributionFailure(p0: String?) {
                    ShowDataTool.showLog("AppsFlyer: onAttributionFailure$p0")
                }

            }, mainStart)
        AppsFlyerLib.getInstance().setCustomerUserId(MainStart.saveBean.appiddata)
        AppsFlyerLib.getInstance().start(mainStart)
        AppsFlyerLib.getInstance().logEvent(mainStart, "wall_photo_install", hashMapOf<String, Any>().apply {
            put("customer_user_id", MainStart.saveBean.appiddata)
            put("app_version", SanPutData.showAppVersion())
            put("os_version", Build.VERSION.RELEASE)
            put("bundle_id", mainStart.packageName)
            put("language", "asc_wds")
            put("platform", "raincoat")
            put("android_id", MainStart.saveBean.appiddata)
        })
    }

    fun initFaceBook() {
        val jsonBean = ShowDataTool.getAdminData()
        val data = jsonBean?.resources?.identifiers?.social?.splitString("_")?.getOrNull(0)?:""
        if (data.isNullOrBlank()) {
            return
        }
        ShowDataTool.showLog("initFaceBook: ${data}")
        FacebookSdk.setApplicationId(data)
        FacebookSdk.sdkInitialize(mainStart)
        AppEventsLogger.activateApp(mainStart)
    }


    fun getFcmFun() {
        if (!mustXS) return
        if (MainStart.saveBean.fcmState) return
        runCatching {
            Firebase.messaging.subscribeToTopic(SanZong.FCM)
                .addOnSuccessListener {
                    MainStart.saveBean.fcmState = true
                    ShowDataTool.showLog("Firebase: subscribe success")
                }
                .addOnFailureListener {
                    ShowDataTool.showLog("Firebase: subscribe fail")
                }
        }
    }

    fun canShowLocked(): Boolean {
        val powerManager = mainStart.getSystemService(Context.POWER_SERVICE) as? PowerManager
        val keyguardManager = mainStart.getSystemService(Context.KEYGUARD_SERVICE) as? KeyguardManager
        if (powerManager == null || keyguardManager == null) {
            return false
        }
        val isScreenOn = powerManager.isInteractive
        val isInKeyguardRestrictedInputMode = keyguardManager.inKeyguardRestrictedInputMode()

        return !isScreenOn || isInKeyguardRestrictedInputMode
    }


     fun adNumAndPoint(): Boolean {
        val adminBean = ShowDataTool.getAdminData()
        if (adminBean == null) {
            ShowDataTool.showLog("AdminBean is null, cannot determine adNumAndPoint")
            return false
        }

        // 从配置中获取最大失败次数
        val maxFailNum = adminBean.appConfig.timing.failure ?:0

        // 如果失败次数超过最大限制且需要重置
        if (MainStart.saveBean.isAdFailCount > maxFailNum && isDifferentDay(
                System.currentTimeMillis()
            )
        ) {
            resetFailureCount() // 重置失败次数
            return true
        }

        return false
    }

    private fun isDifferentDay(currentTime: Long): Boolean {
        val lastReportTime = MainStart.saveBean.lastReportTime
        return !isSameDay(
            lastReportTime,
            currentTime
        )
    }

    private fun isSameDay(time1: Long, time2: Long): Boolean {
        val calendar1 = Calendar.getInstance().apply { timeInMillis = time1 }
        val calendar2 = Calendar.getInstance().apply { timeInMillis = time2 }

        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
                calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)
    }

    private fun resetFailureCount() {
        MainStart.saveBean.isAdFailCount = 0
        MainStart.saveBean.lastReportTime = System.currentTimeMillis()
        ShowDataTool.showLog("Ad failure count has been reset")
    }

}