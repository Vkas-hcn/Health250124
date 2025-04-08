package com.jia.opens.golden.scales.towards.acan

import android.util.Log
import com.google.gson.Gson
import com.jia.opens.golden.scales.towards.jgri6sd.PngJiaBean
import com.jia.opens.golden.scales.towards.pngstart.startApp
import com.jia.opens.golden.scales.towards.vjire.PngAllData

object ShowDataTool {
    fun showLog(msg: String) {
        if (startApp.mustXS) {
            return
        }
        Log.e("PngJia", msg)
    }

    fun getAdminData(): PngJiaBean? {
//        startApp.okSpBean.admindata = PngAllData.data_can
        val adminData = startApp.okSpBean.admindata
        val adminBean = runCatching {
            Gson().fromJson(adminData, PngJiaBean::class.java)
        }.getOrNull()
        return if (adminBean != null && isValidAdminBean(adminBean)) {
            adminBean
        } else {
            null
        }
    }

    private fun isValidAdminBean(bean: PngJiaBean): Boolean {
        return bean.appConfig != null && bean.appConfig.userTier!= null &&
                bean.resources != null && bean.resources.identifiers.social.isNotEmpty()
    }


    fun putAdminData(adminBean: String) {
        startApp.okSpBean.admindata = adminBean

//        startApp.okSpBean.admindata = PngAllData.data_can

    }
}