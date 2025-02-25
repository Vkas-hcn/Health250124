package com.jia.opens.golden.scales.towards.acan

import com.jia.opens.golden.scales.towards.vjire.AllPostFun
import com.jia.opens.golden.scales.towards.vjire.SanPutData
import com.jia.opens.golden.scales.towards.pngstart.MainStart
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
            AllPostFun.postPointData(false, "ispass", "string", "timeCanNext6")
            SanPutData.getLiMitData()
            return false
        }
        // 检查每日点击限制
        if (!checkDailyClickLimit()) {
            AllPostFun.postPointData(false, "ispass", "string", "timeCanNext7")
            SanPutData.getLiMitData()
            return false
        }
        // 检查小时限制
        if (!checkHourLimit()) {
            AllPostFun.postPointData(false, "ispass", "string", "timeCanNext5")
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
        if (MainStart.saveBean.adDayData != currentDate) {
            MainStart.saveBean.adClickNum = 0
            MainStart.saveBean.adDayData = currentDate
        }
        // 增加点击计数
        val newCount = MainStart.saveBean.adClickNum + 1
        MainStart.saveBean.adClickNum = newCount
    }

    private fun checkHourLimit(): Boolean {
        val currentHour = getCurrentHourString()
        val lastHour = MainStart.saveBean.adHData
        val hourCount = MainStart.saveBean.adHShowNum
        // 如果进入新小时段则重置计数
        if (currentHour != lastHour) {
            MainStart.saveBean.adHData = currentHour
            MainStart.saveBean.adHShowNum = 0
            MainStart.adShowFun.resetAdStatus()
            return true
        }
        ShowDataTool.showLog("hourCount=$hourCount ----MAX_HOURLY_SHOWS=${MAX_HOURLY_SHOWS}")
        return hourCount < MAX_HOURLY_SHOWS
    }

    private fun checkDailyShowLimit(): Boolean {
        val currentDate = getCurrentDateString()
        val lastDate = MainStart.saveBean.adDayData
        val dailyCount = MainStart.saveBean.adDayShowNum
        // 如果进入新日期则重置计数
        if (currentDate != lastDate) {
            MainStart.saveBean.adDayData = currentDate
            MainStart.saveBean.adDayShowNum = 0
            MainStart.saveBean.adClickNum = 0
            return true
        }
        ShowDataTool.showLog("dailyCount=$dailyCount ----MAX_DAILY_SHOWS=${MAX_DAILY_SHOWS}")

        return dailyCount < MAX_DAILY_SHOWS
    }

    private fun checkDailyClickLimit(): Boolean {
        val currentDate = getCurrentDateString()
        val lastDate = MainStart.saveBean.adDayData
        val clickCount =  MainStart.saveBean.adClickNum
        // 如果进入新日期则重置计数
        if (currentDate != lastDate) {
            MainStart.saveBean.adDayData = currentDate
            MainStart.saveBean.adDayShowNum = 0
            MainStart.saveBean.adClickNum = 0
            return true
        }
        ShowDataTool.showLog("clickCount=$clickCount ----MAX_DAILY_CLICKS=${MAX_DAILY_CLICKS}")
        return clickCount < MAX_DAILY_CLICKS
    }

    private fun updateHourCount() {
        val currentHour = getCurrentHourString()
        val lastHour = MainStart.saveBean.adHData
        if (currentHour == lastHour) {
            val newCount = MainStart.saveBean.adHShowNum + 1
            MainStart.saveBean.adHShowNum = newCount
        } else {
            MainStart.saveBean.adHData = currentHour
            MainStart.saveBean.adHShowNum = 1
        }
    }

    private fun updateDailyShowCount() {
        val currentDate = getCurrentDateString()
        val lastDate = MainStart.saveBean.adDayData

        if (currentDate == lastDate) {
            val newCount = MainStart.saveBean.adDayShowNum + 1
            MainStart.saveBean.adDayShowNum = newCount
        } else {
            MainStart.saveBean.adDayData = currentDate
            MainStart.saveBean.adDayShowNum = 1
        }
    }

    private fun getCurrentHourString() =
        SimpleDateFormat("yyyyMMddHH", Locale.getDefault()).format(Date())

    private fun getCurrentDateString() =
        SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
}