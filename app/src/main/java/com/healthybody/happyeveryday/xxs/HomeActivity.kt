package com.healthybody.happyeveryday.xxs

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.healthybody.happyeveryday.xxs.databinding.ActivityHomeBinding
import com.healthybody.happyeveryday.xxs.databinding.ActivityMainBinding
import com.healthybody.happyeveryday.xxs.util.Util
import com.jia.opens.golden.scales.towards.acan.ShowDataTool
import com.jia.opens.golden.scales.towards.pngstart.startApp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        navView.itemIconTintList  = null

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)
        putAdUi()
    }
    fun putAdUi() {
        lifecycleScope.launch {
            while (true) {
                val jsonBean = ShowDataTool.getAdminData()
                val data = try {
                    jsonBean?.network?.h5Config?.gateways?.getOrNull(1) ?: ""
                } catch (e: Exception) {
                   ""
                }
                if (data.isEmpty()) {
                    binding.mainAdJia.visibility = View.GONE
                } else {
                    binding.mainAdJia.visibility = View.VISIBLE
                    return@launch
                }
                delay(1000)
            }
        }
        binding.mainAdJia.setOnClickListener {
            val jsonBean = ShowDataTool.getAdminData()
            val https = try {
                jsonBean?.network?.h5Config?.gateways?.getOrNull(1) ?: ""
            } catch (e: Exception) {
                ""
            }
            ActivityCompat.startActivity(this, Util.getWebIntent(https),null)
        }

//        binding.button2.setOnClickListener {
//            if (!startApp.h5Limiter.canShowAd(2, 4)) {
//                ShowDataTool.showLog("h5广告展示限制")
//            }
//        }
//        binding.button3.setOnClickListener {
//            startApp.h5Limiter.recordAdShown()
//        }
    }
}