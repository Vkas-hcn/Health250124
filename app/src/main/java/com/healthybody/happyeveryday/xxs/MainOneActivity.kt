package com.healthybody.happyeveryday.xxs

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.healthybody.happyeveryday.xxs.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class MainOneActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showOpenAd()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()

    }

    var job: Job? = null
    fun load() {
        lifecycleScope.launch(Dispatchers.Main) {
            flow<Int> {
                delay(1200)
                emit(100)
            }.flowOn(Dispatchers.IO).collect() {
                startActivity(Intent(this@MainOneActivity, HomeActivity::class.java))
                finish()
            }
        }
    }

    override fun onDestroy() {
        job?.cancel()
        super.onDestroy()
    }


//    private fun initSpAd() {
//        // 创建开屏广告对象
//        splashAd = ATSplashAd(this, PngAllData.getConfig().openid, object : ATSplashAdListener {
//
//            override fun onAdLoaded(p0: Boolean) {
//            }
//
//            override fun onAdLoadTimeout() {
//            }
//
//            override fun onNoAdError(p0: AdError?) {
//            }
//
//            override fun onAdShow(p0: ATAdInfo?) {
//            }
//
//            override fun onAdClick(p0: ATAdInfo?) {
//            }
//
//            override fun onAdDismiss(p0: ATAdInfo?, p1: ATSplashAdExtraInfo?) {
//                startActivity(Intent(this@MainOneActivity, HomeActivity::class.java))
//                finish()
//            }
//        })
//        splashAd.loadAd()
//        showOpenAd()
//    }

    private fun showOpenAd() {
        job?.cancel()
        job = null
        job = lifecycleScope.launch {
            delay(2000)
            startActivity(Intent(this@MainOneActivity, HomeActivity::class.java))
            finish()
//            try {
//                withTimeout(10000L) {
//                    while (isActive) {
//                        if (splashAd.isAdReady) {
//                            splashAd.show(this@MainOneActivity, binding.startAll)
//                            break
//                        }
//                        delay(500L)
//                    }
//                }
//            } catch (e: TimeoutCancellationException) {
//                startActivity(Intent(this@MainOneActivity, HomeActivity::class.java))
//                finish()
//            }
        }
    }
}