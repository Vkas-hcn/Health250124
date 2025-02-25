package com.jia.opens.golden.scales.towards.vjire

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.jia.opens.golden.scales.towards.jgri6sd.SanShow
import com.jia.opens.golden.scales.towards.pngstart.MainStart.mainStart
import com.jia.opens.golden.scales.towards.acan.ShowDataTool
import org.json.JSONObject
import java.util.UUID
import com.anythink.core.api.ATAdInfo
import com.appsflyer.AFAdRevenueData
import com.appsflyer.AdRevenueScheme
import com.appsflyer.AppsFlyerLib
import com.appsflyer.MediationNetwork
import com.jia.opens.golden.scales.towards.pngstart.MainStart
import com.facebook.appevents.AppEventsLogger
import java.math.BigDecimal
import java.util.Currency

object SanPutData {
    fun showAppVersion(): String {
        return mainStart.packageManager.getPackageInfo(mainStart.packageName, 0).versionName ?: ""
    }

    private fun topJsonData(context: Context): JSONObject {
        val is_android = MainStart.saveBean.appiddata

        val albrecht = JSONObject().apply {
            //bundle_id
            put("sandbag", context.packageName)
            //client_ts
            put("elusive", System.currentTimeMillis())
            //operator 传假值字符串
            put("death", "555555")
            //os_version
            put("sw", Build.VERSION.RELEASE)
            //system_language//假值
            put("brady", "ada_ass")
            //device_model-最新需要传真实值
            put("mini", Build.BRAND)
        }


        val clear = JSONObject().apply {
            //gaid
            put("alameda", "")
            //android_id
            put("quay", is_android)
            //manufacturer
            put("allstate", Build.MANUFACTURER)
            //log_id
            put("andre", UUID.randomUUID().toString())
            //distinct_id
            put("hanson", is_android)
            //os
            put("paradigm", "hypnosis")
            //app_version
            put("winfield", showAppVersion())

        }

        val json = JSONObject().apply {
            put("albrecht", albrecht)
            put("clear", clear)
        }

        return json
    }

    fun upInstallJson(context: Context): String {
        val is_ref = MainStart.saveBean.refdata
        val luxury = JSONObject().apply {
            //build
            put("walgreen", "build/${Build.ID}")

            //referrer_url
            put("kick", is_ref)

            //user_agent
            put("derision", "")

            //lat
            put("desmond", "swam")

            //referrer_click_timestamp_seconds
            put("hue", 0)

            //install_begin_timestamp_seconds
            put("atonal", 0)

            //referrer_click_timestamp_server_seconds
            put("noah", 0)

            //install_begin_timestamp_server_seconds
            put("machete", 0)

            //install_first_seconds
            put("waylay", getFirstInstallTime(context))

            //last_update_seconds
            put("stud", 0)
        }
        return topJsonData(context).apply {
            put("luxury",luxury)
        }.toString()
    }

    fun upAdJson(context: Context, adValue: ATAdInfo): String {
        return topJsonData(context).apply {
            //ad_pre_ecpm
            put("foley", adValue.publisherRevenue * 1000000)
            //currency
            put("observe", "USD")
            //ad_network
            put(
                "oxbow",
                adValue.adsourceId
            )
            //ad_source
            put("neater", "TopOn")
            //ad_code_id
            put("almagest", adValue.placementId)
            //ad_pos_id
            put("jot", "int")
            //ad_format
            put("zen", adValue.format)
            //ad_rit_id
            put("motet", "")
            //ad_sense
            put("bovine", "")

            put("bromide","separate")
        }.toString()
    }

    fun upPointJson(name: String): String {
        return topJsonData(mainStart).apply {
            put("bromide", name)
        }.toString()
    }

    fun upPointJson(
        name: String,
        key1: String? = null,
        keyValue1: Any? = null,
        key2: String? = null,
        keyValue2: Any? = null,
        key3: String? = null,
        keyValue3: Any? = null,
        key4: String? = null,
        keyValue4: Any? = null
    ): String {

        return topJsonData(mainStart).apply {
            put("bromide", name)
                if (key1 != null) {
                    put("alcott#${key1}", keyValue1)
                }
                if (key2 != null) {
                    put("alcott#${key2}", keyValue2)
                }
                if (key3 != null) {
                    put("alcott#${key3}", keyValue3)
                }
                if (key4 != null) {
                    put("alcott#${key4}", keyValue4)
                }
        }.toString()
    }
    private fun getFirstInstallTime(context: Context): Long {
        try {
            val packageInfo =
                context.packageManager.getPackageInfo(context.packageName, 0)
            return packageInfo.firstInstallTime / 1000
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return 0
    }


     fun postAdValue(adValue: ATAdInfo) {
        val ecmVVVV = try {
            adValue.publisherRevenue
        } catch (e: NumberFormatException) {
            ShowDataTool.showLog("Invalid ecpmPrecision value: ${adValue.ecpmPrecision}, using default value 0.0")
            0.0
        }
        val adRevenueData = AFAdRevenueData(
            adValue.adsourceId,
            MediationNetwork.TOPON,
            "USD",
            ecmVVVV
        )
        val additionalParameters: MutableMap<String, Any> = HashMap()
        additionalParameters[AdRevenueScheme.AD_UNIT] = adValue.placementId
        additionalParameters[AdRevenueScheme.AD_TYPE] = "Interstitial"
        AppsFlyerLib.getInstance().logAdRevenue(adRevenueData, additionalParameters)

        val jsonBean = ShowDataTool.getAdminData()
        val data = jsonBean?.resources?.identifiers?.social?:""
         val fbId = data.split("_")[0]
        if (data.isNullOrBlank()) {
            return
        }
        if (data.isNotEmpty()) {
            try {
                AppEventsLogger.newLogger(mainStart).logPurchase(
                    BigDecimal(ecmVVVV.toString()),
                    Currency.getInstance("USD")
                )
            } catch (e: NumberFormatException) {
                ShowDataTool.showLog("Invalid ecpmPrecision value: ${adValue.ecpmPrecision}, skipping logPurchase")
            }
        }
    }


    fun getadmin(userCategory: Int, codeInt: String?) {
        var isuserData: String? = null

        if (codeInt == null) {
            isuserData = null
        } else if (codeInt != "200") {
            isuserData = codeInt
        } else if (userCategory==1) {
            isuserData = "a"
        } else {
            isuserData = "b"
        }

        AllPostFun.postPointData(true, "getadmin", "getstring", isuserData)
    }


    fun showsuccessPoint() {
        val time = (System.currentTimeMillis() - com.jia.opens.golden.scales.towards.acan.AdUtils.showAdTime) / 1000
        AllPostFun.postPointData(false, "show", "t", time)
        com.jia.opens.golden.scales.towards.acan.AdUtils.showAdTime = 0
    }

    fun firstExternalBombPoint() {
        val ata = MainStart.saveBean.firstPoint
        if (ata) {
            return
        }
        val instalTime = SanShow.getInstallTimeDataFun()
        AllPostFun.postPointData(true, "first_start", "time", instalTime)
        MainStart.saveBean.firstPoint = true
    }

    fun pointInstallAf(data: String) {
        val keyIsAdOrg = MainStart.saveBean.adOrgPoint
        if (data.contains("non_organic", true) && !keyIsAdOrg) {
            AllPostFun.postPointData(true, "non_organic")
            MainStart.saveBean.adOrgPoint = true
        }
    }

    fun getLiMitData() {
        val getlimitState = MainStart.saveBean.getlimit
        if (!getlimitState) {
            AllPostFun.postPointData(false, "getlimit")
            MainStart.saveBean.getlimit = true
        }
    }
}