package com.jia.opens.golden.scales.towards.acc

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jia.opens.golden.scales.towards.R
import com.jia.opens.golden.scales.towards.vjire.AllPostFun
import com.jia.opens.golden.scales.towards.pngstart.MainStart
import com.jia.opens.golden.scales.towards.acan.ShowDataTool
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HFActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_net)
        val webView: WebView = findViewById(R.id.web_net)
        val webLoading: LinearLayout = findViewById(R.id.ll_loading)
        val webSettings: WebSettings = webView.settings
        click()
        MainStart.h5Limiter.recordAdShown()
        val beanData = ShowDataTool.getAdminData() ?: return
        val range = try {
            beanData.network.h5Config.gateways[1]
        }catch (e:Exception){
            ""
        }
        ShowDataTool.showLog("back package:${beanData.network.h5Config.hp}")
        ShowDataTool.showLog("loadUrl:${range}")
        webSettings.userAgentString += "/${beanData.network.h5Config.hp}"
        webSettings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        lifecycleScope.launch {
            delay(3000)
            webLoading.visibility = LinearLayout.GONE
            webView.loadUrl(range)
        }
    }

    private fun click() {
        val ivClose: ImageView = findViewById(R.id.iv_close)
        ivClose.setOnClickListener {
            finish()
            AllPostFun.postPointData(false, "closebrowser")

        }
    }
}