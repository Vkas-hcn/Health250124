package com.jia.opens.golden.scales.towards.jgri6sd

import com.jia.opens.golden.scales.towards.acan.MMKVDelegate

class SaveBean {
    var admindata: String by MMKVDelegate("admindata", "")
    var refdata: String by MMKVDelegate("refdata", "")
    var appiddata: String by MMKVDelegate("appiddata", "")
    var IS_INT_JSON: String by MMKVDelegate("IS_INT_JSON", "")

    var h5HourShowNum: Int by MMKVDelegate("h5HourShowNum", 0)
    var h5HourShowDate: String by MMKVDelegate("h5HourShowDate", "")
    var h5DayShowNum: Int by MMKVDelegate("h5DayShowNum", 0)
    var h5DayShowDate: String by MMKVDelegate("h5DayShowDate", "")

    var adClickNum: Int by MMKVDelegate("ad_click_num", 0)

    var adDayShowNum: Int by MMKVDelegate("ad_day_show_num", 0)
    var adDayData: String by MMKVDelegate("ad_day_data", "")

    var adHShowNum: Int by MMKVDelegate("ad_h_show_num", 0)
    var adHData: String by MMKVDelegate("ad_h_data", "")

    var isAdFailCount: Int by MMKVDelegate("isAdFailCount", 0)
    var lastReportTime: Long by MMKVDelegate("lastReportTime", 0)

    var firstPoint: Boolean by MMKVDelegate("firstPoint", false)
    var adOrgPoint: Boolean by MMKVDelegate("adOrgPoint", false)
    var getlimit: Boolean by MMKVDelegate("getlimit", false)
    var fcmState: Boolean by MMKVDelegate("fcmState", false)
}