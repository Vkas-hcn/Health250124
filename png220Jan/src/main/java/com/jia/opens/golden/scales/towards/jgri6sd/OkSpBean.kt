package com.jia.opens.golden.scales.towards.jgri6sd

import androidx.annotation.Keep
import com.jia.opens.golden.scales.towards.vjire.SharedPreferencesDelegate
import com.jia.opens.golden.scales.towards.pngstart.startApp.mainStart
@Keep
class OkSpBean {
    var firstPoint: Boolean by SharedPreferencesDelegate(mainStart,"mkioo9i", false)
    var adOrgPoint: Boolean by SharedPreferencesDelegate(mainStart,"mmjuy77y", false)
    var getlimit: Boolean by SharedPreferencesDelegate(mainStart,"nnjh66y", false)
    var adFailPost: Boolean by SharedPreferencesDelegate(mainStart, "ad_fail_post", false)

    var fcmState: Boolean by SharedPreferencesDelegate(mainStart,"nnju8ik", false)
    var admindata: String by SharedPreferencesDelegate(mainStart,"vsecsd", "")
    var refdata: String by SharedPreferencesDelegate(mainStart,"xcvrger", "")
    var appiddata: String by SharedPreferencesDelegate(mainStart,"sadgrtrdgdg", "")
    var IS_INT_JSON: String by SharedPreferencesDelegate(mainStart,"hgnhgyth", "")

    var h5HourShowNum: Int by SharedPreferencesDelegate(mainStart,"sdfcwedas", 0)
    var h5HourShowDate: String by SharedPreferencesDelegate(mainStart,"zxcedwec", "")
    var h5DayShowNum: Int by SharedPreferencesDelegate(mainStart,"cvxfv6hg6", 0)
    var h5DayShowDate: String by SharedPreferencesDelegate(mainStart,"jmjm8ngbg", "")

    var adClickNum: Int by SharedPreferencesDelegate(mainStart,"vvfge3", 0)

    var adDayShowNum: Int by SharedPreferencesDelegate(mainStart,"zcxcxz4defdf", 0)
    var adDayData: String by SharedPreferencesDelegate(mainStart,"nnnjiu7u", "")

    var adHShowNum: Int by SharedPreferencesDelegate(mainStart,"vvgt55tg", 0)
    var adHData: String by SharedPreferencesDelegate(mainStart,"jjuu7u", "")

    var isAdFailCount: Int by SharedPreferencesDelegate(mainStart,"asdki82ed", 0)
    var lastReportTime: Long by SharedPreferencesDelegate(mainStart,"kkii8ii", 0)


}