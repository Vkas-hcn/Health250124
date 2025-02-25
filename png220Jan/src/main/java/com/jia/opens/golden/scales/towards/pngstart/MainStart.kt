package com.jia.opens.golden.scales.towards.pngstart

import android.annotation.SuppressLint
import android.app.Application
import android.provider.Settings
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.anythink.core.api.ATSDK
import com.jia.opens.golden.scales.towards.jgri6sd.SanLiFun
import com.jia.opens.golden.scales.towards.jgri6sd.SanShow
import com.jia.opens.golden.scales.towards.jgri6sd.SanZong
import com.jia.opens.golden.scales.towards.jgri6sd.SaveBean
import com.jia.opens.golden.scales.towards.vjire.AllPostFun
import com.jia.opens.golden.scales.towards.vjire.NetworkOperator
import com.jia.opens.golden.scales.towards.acan.AdShowFun
import com.jia.opens.golden.scales.towards.acan.HFiveShow
import com.jia.opens.golden.scales.towards.acan.ShowDataTool
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.concurrent.TimeUnit

object MainStart {
    lateinit var mainStart: Application
    var mustXS: Boolean = false
    val adShowFun = AdShowFun()
    val h5Limiter = HFiveShow()
    lateinit var saveBean: SaveBean
    fun init(application: Application, mustXSData: Boolean) {
        if (!SanShow.isMainProcess(application)) {
            return
        }
        MMKV.initialize(application)
        ShowDataTool.showLog("San MainStart init")
        mainStart = application
        val lifecycleObserver = SanLiFun()
        application.registerActivityLifecycleCallbacks(lifecycleObserver)
        initSDKData()
        mustXS = mustXSData
        saveBean = SaveBean()
        WorkManager.initialize(application, Configuration.Builder().build())
        getAndroidId()
        SanShow.startService()
        com.jia.opens.golden.scales.towards.acan.AdUtils.noShowICCC()
        launchRefData()
        com.jia.opens.golden.scales.towards.acan.AdUtils.sessionUp()
        com.jia.opens.golden.scales.towards.acan.AdUtils.initAppsFlyer()
        com.jia.opens.golden.scales.towards.acan.AdUtils.getFcmFun()
    }

    private fun initSDKData() {
        ATSDK.init(mainStart, SanZong.getConfig().appid, SanZong.getConfig().appkey)
        ATSDK.setNetworkLogDebug(!mustXS)
//        GetFFF.loadEncryptedSo(mainStart)
    }

    @SuppressLint("HardwareIds")
    private fun getAndroidId() {
        val adminData = saveBean.appiddata
        if (adminData.isEmpty()) {
            val androidId =
                Settings.Secure.getString(mainStart.contentResolver, Settings.Secure.ANDROID_ID)
            if (!androidId.isNullOrBlank()) {
                saveBean.appiddata = androidId
            } else {
                saveBean.appiddata = UUID.randomUUID().toString()
            }
        }
    }

    private fun launchRefData() {
        val refData = saveBean.refdata
        val intJson = saveBean.IS_INT_JSON
        if (refData.isNotEmpty()) {
            startOneTimeAdminData()
            intJson.takeIf { it.isNotEmpty() }?.let {
                AllPostFun.postInstallData(mainStart)
            }
            return
        }
        ShowDataTool.showLog("launchRefData=$refData")
        startRefDataCheckLoop()
    }

    private fun startRefDataCheckLoop() {
        CoroutineScope(Dispatchers.IO).launch {
            while (saveBean.refdata.isEmpty()) {
                refInformation()
                delay(10100)
            }
        }
    }

    private fun refInformation() {
        runCatching {
            val referrerClient = InstallReferrerClient.newBuilder(mainStart).build()
            referrerClient.startConnection(object : InstallReferrerStateListener {
                override fun onInstallReferrerSetupFinished(responseCode: Int) {
                    handleReferrerSetup(responseCode, referrerClient)
                }

                override fun onInstallReferrerServiceDisconnected() {
                }
            })
        }.onFailure { e ->
            ShowDataTool.showLog("Failed to fetch referrer: ${e.message}")
        }
    }

    private fun handleReferrerSetup(responseCode: Int, referrerClient: InstallReferrerClient) {
        when (responseCode) {
            InstallReferrerClient.InstallReferrerResponse.OK -> {
                val installReferrer = referrerClient.installReferrer.installReferrer
                if (installReferrer.isNotEmpty()) {
                    saveBean.refdata = installReferrer
                    AllPostFun.postInstallData(mainStart)
                    startOneTimeAdminData()
                }
                ShowDataTool.showLog("Referrer  data: ${installReferrer}")
            }

            else -> {
                ShowDataTool.showLog("Failed to setup referrer: $responseCode")
            }
        }

        kotlin.runCatching {
            referrerClient.endConnection()
        }
    }

    private fun startOneTimeAdminData() {
        val adminData = saveBean.admindata
        ShowDataTool.showLog("startOneTimeAdminData: $adminData")
        AllPostFun.onePostAdmin(adminData.isEmpty())
        //1hours
        scheduleHourlyAdminRequest()
    }


    private fun scheduleHourlyAdminRequest() {
        val workRequest = PeriodicWorkRequestBuilder<OneHW>(1, TimeUnit.HOURS)
            .setInitialDelay(1, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(mainStart).enqueueUniquePeriodicWork(
            "AdminRequestWork",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }

    fun canIntNextFun() {
        adShowFun.startRomFun()
    }

    //对字符串进行分割
    fun String.splitString(delimiter: String): List<String> {
        require(delimiter.isNotEmpty()) { "Delimiter cannot be empty" }
        return this.split(delimiter.toRegex(RegexOption.LITERAL))
    }


}