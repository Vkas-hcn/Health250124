package com.jia.opens.golden.scales.towards.acc

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jia.opens.golden.scales.towards.jgri6sd.SanShow
import com.jia.opens.golden.scales.towards.vjire.AllPostFun
import com.jia.opens.golden.scales.towards.vjire.SanPutData
import com.jia.opens.golden.scales.towards.pngstart.MainStart
import com.jia.opens.golden.scales.towards.pngstart.MainStart.adShowFun
import com.jia.opens.golden.scales.towards.acan.ShowDataTool
import com.jia.opens.golden.scales.towards.pngstart.MainStart.splitString
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class BulletsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ShowDataTool.showLog("BulletsActivity")
        MainStart.saveBean.isAdFailCount = 0
        determineContentType()
    }

    private fun determineContentType() {
        if (SanShow.isH5State) {
            val h5Intent = Intent(this, HFActivity::class.java)
            startActivity(h5Intent)
            AllPostFun.postPointData(false, "browserjump")
        } else {
            SanPutData.firstExternalBombPoint()
            displayAdvertisement()
        }
    }

    private fun displayAdvertisement() {
        if (adShowFun.interstitialAd.isAdReady) {
            AllPostFun.postPointData(false, "isready")
            val delayDuration = generateRandomDelay()
            ShowDataTool.showLog("Advertisement display delay duration: $delayDuration")
            AllPostFun.postPointData(false, "starup", "time", delayDuration / 1000)
          lifecycleScope.launch {
                delay(delayDuration)
                AllPostFun.postPointData(false, "delaytime", "time", delayDuration / 1000)
                com.jia.opens.golden.scales.towards.acan.AdUtils.showAdTime = System.currentTimeMillis()
                com.jia.opens.golden.scales.towards.acan.AdUtils.adShowTime = System.currentTimeMillis()
                adShowFun.interstitialAd.show(this@BulletsActivity)
                trackAdSuccessAfter30Seconds()
            }
        } else {
            finish()
        }
    }


    private fun trackAdSuccessAfter30Seconds() {
        lifecycleScope.launch {
            delay(30000)
            if (com.jia.opens.golden.scales.towards.acan.AdUtils.showAdTime > 0) {
                AllPostFun.postPointData(false, "show", "t", "30")
                com.jia.opens.golden.scales.towards.acan.AdUtils.showAdTime = 0
            }
        }
    }

    private fun generateRandomDelay(): Long {
        val adminData = ShowDataTool.getAdminData()
        val delayRange = adminData?.resources?.delayRange
        return if (delayRange != null) {
            val minDelay = delayRange.splitString("~").getOrNull(0)?.toLong()?:0L
            val maxDelay = delayRange.splitString("~").getOrNull(1)?.toLong()?:0L
            Random.nextLong(minDelay, maxDelay + 1)
        } else {
            Random.nextLong(2000, 3000 + 1)
        }
    }
}