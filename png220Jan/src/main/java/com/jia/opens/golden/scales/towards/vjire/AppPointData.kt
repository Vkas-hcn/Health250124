package com.jia.opens.golden.scales.towards.vjire

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.jia.opens.golden.scales.towards.pngstart.startApp.mainStart
import com.jia.opens.golden.scales.towards.acan.ShowDataTool
import org.json.JSONObject
import java.util.UUID
import com.anythink.core.api.ATAdInfo
import com.appsflyer.AFAdRevenueData
import com.appsflyer.AdRevenueScheme
import com.appsflyer.AppsFlyerLib
import com.appsflyer.MediationNetwork
import com.jia.opens.golden.scales.towards.pngstart.startApp
import com.facebook.appevents.AppEventsLogger
import java.math.BigDecimal
import java.util.Currency

object AppPointData {
    fun showAppVersion(): String {
        return mainStart.packageManager.getPackageInfo(mainStart.packageName, 0).versionName ?: ""
    }

    private fun topJsonData(context: Context): JSONObject {
        val is_android = startApp.okSpBean.appiddata

        val dorcas = JSONObject().apply {
            //device_model-最新需要传真实值
            put("spiro", Build.BRAND)
            //log_id
            put("kidnap", UUID.randomUUID().toString())
        }


        val talcum = JSONObject().apply {
            //os
            put("forlorn", "imperate")
            //manufacturer
            put("refusal", Build.MANUFACTURER)
            //system_language//假值
            put("dow", "asd_sml")
            //app_version
            put("slocum", showAppVersion())
            //android_id
            put("retinal", is_android)
        }

        val sneaky = JSONObject().apply {
            //bundle_id
            put("dossier", context.packageName)
            //operator 传假值字符串
            put("sever", "666666")
            //client_ts
            put("truss", System.currentTimeMillis())
            //os_version
            put("mcgregor", Build.VERSION.RELEASE)
            //distinct_id
            put("tudor", is_android)
            //gaid
            put("front", "")
        }

        val json = JSONObject().apply {
            put("dorcas", dorcas)
            put("talcum", talcum)
            put("sneaky", sneaky)
        }

        return json
    }

    fun upInstallJson(context: Context): String {
        val is_ref = startApp.okSpBean.refdata
        val tori = JSONObject().apply {
            //build
            put("algebra", "build/${Build.ID}")

            //referrer_url
            put("fascicle", is_ref)

            //user_agent
            put("tractor", "")

            //lat
            put("coast", "phosgene")

            //referrer_click_timestamp_seconds
            put("varitype", 0)

            //install_begin_timestamp_seconds
            put("snappish", 0)

            //referrer_click_timestamp_server_seconds
            put("forget", 0)

            //install_begin_timestamp_server_seconds
            put("prefer", 0)

            //install_first_seconds
            put("gusty", getFirstInstallTime(context))

            //last_update_seconds
            put("policy", 0)
        }
        return topJsonData(context).apply {
            put("tori",tori)
        }.toString()
    }

    fun upAdJson(context: Context, adValue: ATAdInfo): String {
        val simplex = JSONObject().apply {
            //ad_pre_ecpm
            put("jackass", adValue.publisherRevenue * 1000000)
            //currency
            put("happy", "USD")
            //ad_network
            put(
                "business",
                adValue.adsourceId
            )
            //ad_source
            put("ammo", "TopOn")
            //ad_code_id
            put("glottal", adValue.placementId)
            //ad_pos_id
            put("pacemake", "int")

            //ad_rit_id
            put("knurl", "")
            //ad_sense
            put("crusoe", "")
            //ad_format
            put("molal", adValue.format)
        }
        return topJsonData(context).apply {
            put("simplex",simplex)

        }.toString()
    }

    fun upPointJson(name: String): String {
        return topJsonData(mainStart).apply {
            put("abetted", name)
        }.toString()
    }

    fun upPointJson(
        name: String,
        key1: String? = null,
        keyValue1: Any? = null,
        key2: String? = null,
        keyValue2: Any? = null,
    ): String {

        return topJsonData(mainStart).apply {
            put("abetted", name)
                if (key1 != null) {
                    put("${key1}#inveigle", keyValue1)
                }
                if (key2 != null) {
                    put("${key2}#inveigle", keyValue2)
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
        if (fbId.isBlank()) {
            return
        }
        if (fbId.isNotEmpty()) {
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

        TtPoint.postPointData(true, "getadmin", "getstring", isuserData)
    }


    fun showsuccessPoint() {
        val time = (System.currentTimeMillis() - com.jia.opens.golden.scales.towards.acan.TopOnUtils.showAdTime) / 1000
        TtPoint.postPointData(false, "show", "t", time)
        com.jia.opens.golden.scales.towards.acan.TopOnUtils.showAdTime = 0
    }

    fun firstExternalBombPoint() {
        val ata = startApp.okSpBean.firstPoint
        if (ata) {
            return
        }
        val instalTime = PngCanGo.getInstallTimeDataFun()
        TtPoint.postPointData(true, "first_start", "time", instalTime)
        startApp.okSpBean.firstPoint = true
    }

    fun pointInstallAf(data: String) {
        val keyIsAdOrg = startApp.okSpBean.adOrgPoint
        if (data.contains("non_organic", true) && !keyIsAdOrg) {
            TtPoint.postPointData(true, "non_organic")
            startApp.okSpBean.adOrgPoint = true
        }
    }

    fun getLiMitData() {
        val getlimitState = startApp.okSpBean.getlimit
        if (!getlimitState) {
            TtPoint.postPointData(false, "getlimit")
            startApp.okSpBean.getlimit = true
        }
    }
}