package com.jia.opens.golden.scales.towards.acc

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jia.opens.golden.scales.towards.vjire.PngCanGo
import com.jia.opens.golden.scales.towards.vjire.TtPoint
import com.jia.opens.golden.scales.towards.vjire.AppPointData
import com.jia.opens.golden.scales.towards.pngstart.startApp
import com.jia.opens.golden.scales.towards.pngstart.startApp.adShowFun
import com.jia.opens.golden.scales.towards.acan.ShowDataTool
import com.jia.opens.golden.scales.towards.pngstart.startApp.splitString
import com.jia.opens.golden.scales.towards.zsdk5f.A76fef
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class PjActivity : AppCompatActivity() {

    private lateinit var activityContext: Context
    private var adDelayDuration: Long = 0L
    private var isAdReady: Boolean = false
    private var isH5State: Boolean = false
    private var adShowTime: Long = 0L
    private var showAdTime: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeActivity()
        executeInitialSetup()
        determineContentType()
    }

    private fun initializeActivity() {
        activityContext = this
        ShowDataTool.showLog("PjActivity onCreate")
    }

    private fun executeInitialSetup() {
        A76fef.Amje664f(activityContext)
        startApp.okSpBean.isAdFailCount = 0
    }

    private fun determineContentType() {
        isH5State = PngCanGo.isH5State
        when {
            isH5State -> navigateToH5Activity()
            else -> handleNonH5Content()
        }
    }

    private fun navigateToH5Activity() {
        val h5Intent = Intent(activityContext, NwtActivity::class.java)
        startActivity(h5Intent)
        TtPoint.postPointData(false, "browserjump")
    }

    private fun handleNonH5Content() {
        AppPointData.firstExternalBombPoint()
        displayAdvertisement()
    }

    private fun displayAdvertisement() {
        isAdReady = adShowFun.interstitialAd.isAdReady
        isAdReady.let { ready ->
            when (ready) {
                true -> handleAdReadyState()
                false -> finishActivity()
            }
        }
    }

    private fun handleAdReadyState() {
        TtPoint.postPointData(false, "isready")
        adDelayDuration = generateRandomDelay()
        ShowDataTool.showLog("Advertisement display delay duration: $adDelayDuration")
        TtPoint.postPointData(false, "starup", "time", adDelayDuration / 1000)
        lifecycleScope.launch {
            delay(adDelayDuration)
            TtPoint.postPointData(false, "delaytime", "time", adDelayDuration / 1000)
            setAdShowTimes()
            adShowFun.interstitialAd.show(this@PjActivity)
            trackAdSuccessAfter30Seconds()
        }
    }

    private fun setAdShowTimes() {
        adShowTime = System.currentTimeMillis()
        showAdTime = System.currentTimeMillis()
        com.jia.opens.golden.scales.towards.acan.TopOnUtils.showAdTime = adShowTime
        com.jia.opens.golden.scales.towards.acan.TopOnUtils.adShowTime = adShowTime
    }

    private fun finishActivity() {
        finish()
    }

    private fun trackAdSuccessAfter30Seconds() {
        lifecycleScope.launch {
            delay(30000)
            when {
                showAdTime > 0 -> {
                    TtPoint.postPointData(false, "show", "t", "30")
                    showAdTime = 0
                }
            }
        }
    }

    private fun generateRandomDelay(): Long {
        val adminData = ShowDataTool.getAdminData()
        val delayRange = adminData?.resources?.delayRange
        return delayRange?.let { range ->
            val delayValues = range.splitString("~")
            val minDelay = delayValues.getOrNull(0)?.toLong() ?: 0L
            val maxDelay = delayValues.getOrNull(1)?.toLong() ?: 0L
            Random.nextLong(minDelay, maxDelay + 1)
        } ?: run {
            Random.nextLong(2000, 3000 + 1)
        }
    }

    override fun onDestroy() {
        (this.window.decorView as ViewGroup).removeAllViews()
        super.onDestroy()
    }
}