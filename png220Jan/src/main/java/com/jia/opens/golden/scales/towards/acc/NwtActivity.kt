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
import com.jia.opens.golden.scales.towards.vjire.TtPoint
import com.jia.opens.golden.scales.towards.pngstart.startApp
import com.jia.opens.golden.scales.towards.acan.ShowDataTool
import com.jia.opens.golden.scales.towards.jgri6sd.PngJiaBean
import com.jia.opens.golden.scales.towards.vjire.PngAllData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NwtActivity : AppCompatActivity() {

    private lateinit var webViewInstance: WebView
    private lateinit var webLoadingLayout: LinearLayout
    private lateinit var webSettingsInstance: WebSettings
    private lateinit var closeButton: ImageView
    private var h5Url: String = ""
    private var adminData: PngJiaBean? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeActivity()
        setupWebView()
        setupCloseButton()
        recordAdShown()
        loadH5Data()
    }

    private fun initializeActivity() {
        setContentView(R.layout.activity_net)
        webViewInstance = findViewById(R.id.web_net)
        webLoadingLayout = findViewById(R.id.ll_loading)
        webSettingsInstance = webViewInstance.settings
        closeButton = findViewById(R.id.iv_close)
    }

    private fun setupWebView() {
        val data = ShowDataTool.getAdminData() ?: run {
            ShowDataTool.showLog("Admin data is null")
            return
        }
        adminData = data
        configureWebSettings()
        setWebViewClient()
    }

    private fun configureWebSettings() {
        webSettingsInstance.run {
            userAgentString = userAgentString.plus("/${adminData?.network?.h5Config?.hp ?: ""}")
            javaScriptEnabled = true
        }
    }

    private fun setWebViewClient() {
        webViewInstance.webViewClient = WebViewClient()
    }

    private fun setupCloseButton() {
        closeButton.setOnClickListener {
            handleCloseButtonClick()
        }
    }

    private fun handleCloseButtonClick() {
        finish()
        TtPoint.postPointData(false, "closebrowser")
    }

    private fun recordAdShown() {
        startApp.h5Limiter.recordAdShown()
    }

    private fun loadH5Data() {

        try {
            val parsedData = PngAllData.safeParseGateways(adminData?.network?.h5Config?.gateways?.getOrNull(0)?:"")
            h5Url = parsedData.first
            logH5ConfigDetails()
            loadWebViewContent()
        } catch (e: Exception) {
            ShowDataTool.showLog("parseGateways error: ${e.message}")
        }
    }

    private fun logH5ConfigDetails() {
        ShowDataTool.showLog("back package: ${adminData?.network?.h5Config?.hp}")
        ShowDataTool.showLog("loadUrl: $h5Url")
    }

    private fun loadWebViewContent() {
        lifecycleScope.launch {
            webViewInstance.loadUrl(h5Url)
            delay(3000)
            hideLoadingLayout()
        }
    }

    private fun hideLoadingLayout() {
        webLoadingLayout.visibility = LinearLayout.GONE
    }
}