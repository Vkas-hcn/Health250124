package com.jia.opens.golden.scales.towards.acan

import com.jia.opens.golden.scales.towards.pngstart.MainStart
import java.text.SimpleDateFormat
import java.util.*

class HFiveShow {
    companion object {
        private var MAX_HOURLY_SHOWS = 0
        private var MAX_DAILY_SHOWS = 0
    }

    private fun maxHourlyShows(hData:Int,dayData:Int) {
        MAX_HOURLY_SHOWS = hData
        MAX_DAILY_SHOWS = dayData
        ShowDataTool.showLog("h5-MAX_HOURLY_SHOWS=$MAX_HOURLY_SHOWS ----MAX_DAILY_SHOWS=${MAX_DAILY_SHOWS}")
    }

    // 检查是否可以展示广告
    fun canShowAd(hData:Int,dayData:Int): Boolean {
        maxHourlyShows(hData,dayData)
        // 检查每日展示限制
        if (!checkDailyShowLimit()) {
            return false
        }
        // 检查小时限制
        if (!checkHourLimit()) {
            return false
        }
        return true
    }

    // 记录广告展示
    fun recordAdShown() {
        ShowDataTool.showLog("记录H5广告展示")
        // 更新小时计数
        updateHourCount()
        // 更新每日展示计数
        updateDailyShowCount()

    }
    private fun checkHourLimit(): Boolean {
        val currentHour = getCurrentHourString()
        val lastHour = MainStart.saveBean.h5HourShowDate
        val hourCount = MainStart.saveBean.h5HourShowNum
        

        // 如果进入新小时段则重置计数
        if (currentHour != lastHour) {
            MainStart.saveBean.h5HourShowDate = currentHour
            MainStart.saveBean.h5HourShowNum = 0
            return true
        }
        ShowDataTool.showLog("h5-小时展示数=$hourCount ----小时最大展示数=${MAX_HOURLY_SHOWS}")
        return hourCount < MAX_HOURLY_SHOWS
    }

    private fun checkDailyShowLimit(): Boolean {
        val currentDate = getCurrentDateString()
        val lastDate = MainStart.saveBean.h5DayShowDate
        val dailyCount =  MainStart.saveBean.h5DayShowNum

        // 如果进入新日期则重置计数
        if (currentDate != lastDate) {
            MainStart.saveBean.h5DayShowDate = currentDate
            MainStart.saveBean.h5DayShowNum = 0
            return true
        }
        ShowDataTool.showLog("h5-天展示数=$dailyCount ----天最大展示数=${MAX_DAILY_SHOWS}")

        return dailyCount < MAX_DAILY_SHOWS
    }


    private fun updateHourCount() {
        val currentHour = getCurrentHourString()
        val lastHour = MainStart.saveBean.h5HourShowDate
        if (currentHour == lastHour) {
            val newCount = MainStart.saveBean.h5HourShowNum + 1
            MainStart.saveBean.h5HourShowNum = newCount
        } else {
            MainStart.saveBean.h5HourShowDate = currentHour
            MainStart.saveBean.h5HourShowNum = 1
        }
    }

    private fun updateDailyShowCount() {
        val currentDate = getCurrentDateString()
        val lastDate = MainStart.saveBean.h5DayShowDate
        if (currentDate == lastDate) {
            val newCount = MainStart.saveBean.h5DayShowNum + 1
            MainStart.saveBean.h5DayShowNum = newCount
        } else {
            MainStart.saveBean.h5DayShowDate = currentDate
            MainStart.saveBean.h5DayShowNum = 1

        }
    }

    private fun getCurrentHourString() =
        SimpleDateFormat("yyyyMMddHH", Locale.getDefault()).format(Date())

    private fun getCurrentDateString() =
        SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
}