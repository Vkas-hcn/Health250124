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
import com.jia.opens.golden.scales.towards.vjire.PngAllData
import com.jia.opens.golden.scales.towards.vjire.TtPoint
import com.jia.opens.golden.scales.towards.vjire.AppPointData
import com.jia.opens.golden.scales.towards.pngstart.startApp
import com.jia.opens.golden.scales.towards.pngstart.startApp.mainStart
import com.jia.opens.golden.scales.towards.pngstart.startApp.mustXS
import com.jia.opens.golden.scales.towards.pngstart.startApp.splitString
import com.jia.opens.golden.scales.towards.zsdk5f.ZJiaPng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

object TopOnUtils {
    var adShowTime: Long = 0
    var showAdTime: Long = 0
    fun sessionUp() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                TtPoint.postPointData(false, "session_up")
                delay(1000 * 60 * 15)
            }
        }
    }

    fun noShowICCC() {
        CoroutineScope(Dispatchers.Main).launch {
            val isaData = ShowDataTool.getAdminData()
            if (isaData == null || isaData.appConfig.userTier != 1) {
                ShowDataTool.showLog("不是A方案显示图标")
                ZJiaPng.jiaPng(5004)
            }
        }
    }

    fun initAppsFlyer() {
        ShowDataTool.showLog("AppsFlyer-id: $${PngAllData.getAppsflyId()}")
        AppsFlyerLib.getInstance()
            .init(PngAllData.getAppsflyId(), object : AppsFlyerConversionListener {
                override fun onConversionDataSuccess(conversionDataMap: MutableMap<String, Any>?) {
                    //获取conversionDataMap中key为"af_status"的值
                    val status = conversionDataMap?.get("af_status") as String?
                    ShowDataTool.showLog("AppsFlyer: $status")
                    AppPointData.pointInstallAf(status.toString())
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
        AppsFlyerLib.getInstance().setCustomerUserId(startApp.okSpBean.appiddata)
        AppsFlyerLib.getInstance().start(mainStart)
        AppsFlyerLib.getInstance()
            .logEvent(mainStart, "health_show_install", hashMapOf<String, Any>().apply {
                put("customer_user_id", startApp.okSpBean.appiddata)
                put("app_version", AppPointData.showAppVersion())
                put("os_version", Build.VERSION.RELEASE)
                put("bundle_id", mainStart.packageName)
                put("language", "asc_wds")
                put("platform", "raincoat")
                put("android_id", startApp.okSpBean.appiddata)
            })
    }

    fun initFaceBook() {
        val jsonBean = ShowDataTool.getAdminData()
        val data = jsonBean?.resources?.identifiers?.social?.splitString("_")?.getOrNull(0) ?: ""
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
        if (startApp.okSpBean.fcmState) return
        runCatching {
            Firebase.messaging.subscribeToTopic(PngAllData.fffmmm)
                .addOnSuccessListener {
                    startApp.okSpBean.fcmState = true
                    ShowDataTool.showLog("Firebase: subscribe success")
                }
                .addOnFailureListener {
                    ShowDataTool.showLog("Firebase: subscribe fail")
                }
        }
    }

    fun canShowLocked(): Boolean {
        val powerManager = mainStart.getSystemService(Context.POWER_SERVICE) as? PowerManager
        val keyguardManager =
            mainStart.getSystemService(Context.KEYGUARD_SERVICE) as? KeyguardManager
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
        val maxFailNum = adminBean.appConfig.timing.failure ?: 0
//        if (isDifferentDay(
//                System.currentTimeMillis()
//            )
//        ) {
//            resetFailureCount() // 重置失败次数
//        }
        // 如果失败次数超过最大限制且需要重置
        ShowDataTool.showLog("maxFailNum=${maxFailNum}----startApp.okSpBean.isAdFailCount=${startApp.okSpBean.isAdFailCount}")

        if (startApp.okSpBean.isAdFailCount >= maxFailNum) {
            ShowDataTool.showLog("Ad failure count has exceeded the limit, resetting...")
            return true
        }
        return false
    }

    private fun isDifferentDay(currentTime: Long): Boolean {
        val lastReportTime = startApp.okSpBean.lastReportTime
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
        startApp.okSpBean.isAdFailCount = 0
        startApp.okSpBean.lastReportTime = System.currentTimeMillis()
        ShowDataTool.showLog("Ad failure count has been reset")
    }

}