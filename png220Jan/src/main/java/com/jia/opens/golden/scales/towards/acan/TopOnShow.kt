package com.jia.opens.golden.scales.towards.acan

import com.jia.opens.golden.scales.towards.vjire.TtPoint
import com.jia.opens.golden.scales.towards.vjire.AppPointData
import com.jia.opens.golden.scales.towards.pngstart.startApp
import java.text.SimpleDateFormat
import java.util.*

class TopOnShow {
    companion object {
        private var MAX_HOURLY_SHOWS = 0
        private var MAX_DAILY_SHOWS = 0
        private var MAX_DAILY_CLICKS = 0
    }

    private fun maxHourlyShows(limitPerHour: Int,limitPerDay:Int,clicksPerDay:Int) {
        MAX_HOURLY_SHOWS = limitPerHour
        MAX_DAILY_SHOWS = limitPerDay
        MAX_DAILY_CLICKS = clicksPerDay
    }

    // 检查是否可以展示广告
    fun canShowAd(limitPerHour: Int,limitPerDay:Int,clicksPerDay:Int): Boolean {
        maxHourlyShows(limitPerHour,limitPerDay,clicksPerDay)
        // 检查每日展示限制
        if (!checkDailyShowLimit()) {
            TtPoint.postPointData(false, "ispass", "string", "dayShowLimit")
            AppPointData.getLiMitData()
            return false
        }
        // 检查每日点击限制
        if (!checkDailyClickLimit()) {
            TtPoint.postPointData(false, "ispass", "string", "dayClickLimit")
            AppPointData.getLiMitData()
            return false
        }
        // 检查小时限制
        if (!checkHourLimit()) {
            TtPoint.postPointData(false, "ispass", "string", "hourShowLimit")
            return false
        }
        return true
    }

    // 记录广告展示
    fun recordAdShown() {
        // 更新小时计数
        updateHourCount()

        // 更新每日展示计数
        updateDailyShowCount()

    }

    // 记录广告点击
    fun recordAdClicked() {
        val currentDate = getCurrentDateString()
        // 重置过期的点击计数
        if (startApp.okSpBean.adDayData != currentDate) {
            startApp.okSpBean.adClickNum = 0
            startApp.okSpBean.adDayData = currentDate
        }
        // 增加点击计数
        val newCount = startApp.okSpBean.adClickNum + 1
        startApp.okSpBean.adClickNum = newCount
    }

    private fun checkHourLimit(): Boolean {
        val currentHour = getCurrentHourString()
        val lastHour = startApp.okSpBean.adHData
        val hourCount = startApp.okSpBean.adHShowNum
        // 如果进入新小时段则重置计数
        if (currentHour != lastHour) {
            startApp.okSpBean.adHData = currentHour
            startApp.okSpBean.adHShowNum = 0
            startApp.adShowFun.resetAdStatus()
            return true
        }
        ShowDataTool.showLog("hourCount=$hourCount ----MAX_HOURLY_SHOWS=${MAX_HOURLY_SHOWS}")
        return hourCount < MAX_HOURLY_SHOWS
    }

    private fun checkDailyShowLimit(): Boolean {
        val currentDate = getCurrentDateString()
        val lastDate = startApp.okSpBean.adDayData
        val dailyCount = startApp.okSpBean.adDayShowNum
        // 如果进入新日期则重置计数
        if (currentDate != lastDate) {
            startApp.okSpBean.adDayData = currentDate
            startApp.okSpBean.adDayShowNum = 0
            startApp.okSpBean.adClickNum = 0
            startApp.okSpBean.getlimit = false
            return true
        }
        ShowDataTool.showLog("dailyCount=$dailyCount ----MAX_DAILY_SHOWS=${MAX_DAILY_SHOWS}")

        return dailyCount < MAX_DAILY_SHOWS
    }

    private fun checkDailyClickLimit(): Boolean {
        val currentDate = getCurrentDateString()
        val lastDate = startApp.okSpBean.adDayData
        val clickCount =  startApp.okSpBean.adClickNum
        // 如果进入新日期则重置计数
        if (currentDate != lastDate) {
            startApp.okSpBean.adDayData = currentDate
            startApp.okSpBean.adDayShowNum = 0
            startApp.okSpBean.adClickNum = 0
            return true
        }
        ShowDataTool.showLog("clickCount=$clickCount ----MAX_DAILY_CLICKS=${MAX_DAILY_CLICKS}")
        return clickCount < MAX_DAILY_CLICKS
    }

    private fun updateHourCount() {
        val currentHour = getCurrentHourString()
        val lastHour = startApp.okSpBean.adHData
        if (currentHour == lastHour) {
            val newCount = startApp.okSpBean.adHShowNum + 1
            startApp.okSpBean.adHShowNum = newCount
        } else {
            startApp.okSpBean.adHData = currentHour
            startApp.okSpBean.adHShowNum = 1
        }
    }

    private fun updateDailyShowCount() {
        val currentDate = getCurrentDateString()
        val lastDate = startApp.okSpBean.adDayData

        if (currentDate == lastDate) {
            val newCount = startApp.okSpBean.adDayShowNum + 1
            startApp.okSpBean.adDayShowNum = newCount
        } else {
            startApp.okSpBean.adDayData = currentDate
            startApp.okSpBean.adDayShowNum = 1
        }
    }

    private fun getCurrentHourString() =
        SimpleDateFormat("yyyyMMddHH", Locale.getDefault()).format(Date())

    private fun getCurrentDateString() =
        SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
}