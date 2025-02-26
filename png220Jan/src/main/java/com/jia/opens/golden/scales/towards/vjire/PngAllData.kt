package com.jia.opens.golden.scales.towards.vjire

import androidx.annotation.Keep
import com.jia.opens.golden.scales.towards.pngstart.startApp

@Keep
object PngAllData {
    const val aeskey = "fjieut65ghrvk7we"

    const val fffmmm = "ncie7cwec"
    fun getConfig(isXS: Boolean = startApp.mustXS): Config {
        return Config(
            appid = if (!isXS) "h670e13c4e3ab6" else "h67bd74cf66f09",
            appkey = if (!isXS) "ac360a993a659579a11f6df50b9e78639" else "a6621ff84d9dd7a8ef1f507323feaf5e2",
            openid = if (!isXS) "n1fvmhio0uchmj" else "n1g9r3q806blkd",
            upUrl = if (!isXS) "https://test-carlyle.bpressuresugarkeeper.com/charley/julio" else "https://carlyle.bpressuresugarkeeper.com/anomaly/ramrod",
            adminUrl = if (!isXS) "https://kapa.bpressuresugarkeeper.com/apitest/pngs/" else "https://kapa.bpressuresugarkeeper.com/api/pngs/"
        )
    }

    data class Config(
        val appid: String,
        val appkey: String,
        val openid: String,
        val upUrl: String,
        val adminUrl: String
    )

    fun getAppsflyId(): String {
        return if (!startApp.mustXS) {
            "5MiZBZBjzzChyhaowfLpyR"
        } else {
            "TQ7rG3upZjUNb9FjGYUfsg"
        }
    }

    const val local_admin_json2 = """
{
    "userCategory": "A",//A/B 用户类型
    "canUpload": true,// 上传权限开关
    "advertisementSettings": {
        "intervalCheck": 10,// 定时检测时间(秒)
        "initialDelay": 60,// 安装后首次展示延迟(秒)
        "intervalShow": 10,// 广告展示间隔(秒)
        "limitPerHour": 100,// 小时展示上限
        "limitPerDay": 3,// 天展示上限
        "clicksPerDay": 10, // 天点击上限
        "cantNum":100 //失败次数
    },
    "adAssets": {
        "adIdentifier": "n1fvkei1g11lcv",// 广告ID
        "fbAdPlacement": "3616318175247400",// FB ID
        "externalResourcePath": "/sanfkkt"// 外弹文件路径
    },
    "delaySettings": {
        "minimumDelay": 2000,// 随机延迟下限(秒)
        "maximumDelay": 3000// 随机延迟上限(秒)
    },
    "net": {
        "sanTT": "10",/前N秒
        "sanpk": "com",//需传包名给h5
        "sanU1": "https://www.baidu.com",//体外H5广告链接配置
        "sanU2": "https://www.google.com",//体内H5广告链接配置
        "sanMaxH": "2",//单小时跳转次数
        "sanMaxD": "5"//单日跳转次数
    }
}
"""

    const val local_admin_json1 = """
{
  "appConfig": {
    "userTier": 1,//1:A用户类型,其他B
    "dataSync": true,// 上传权限开关
    "timing": {
      "checks": "10|60|10", // 定时检测|首次延迟|展示间隔
      "failure": 100 ////失败次数
    },
    "exposure": {
      "limits": "5/10",    // 小时/天 展示上限
      "interactions": 10 // 天点击上限
    }
  },
  "resources": {
    "identifiers": {
      "internal": "n1fvkei1g11lcv",// 广告ID
      "social": "3616318175247400_pngjia" // FB ID _ 路径（_号分割）
    },
    "delayRange": "2000~3000" // 延迟范围
  },
  "network": {
    "h5Config": {
      "hp": "com",//包名
      "ttl": 10, // 前N秒
      "gateways": [
        "https://www.baidu.com=>2|5",      // 体外链接=>小时|天限制
        "https://www.google.com"      // 体内链接
      ]
    }
  }
}

    """
    const val data_can = """
        {
    "appConfig": {
        "userTier": 1,
        "dataSync": true,
        "timing": {
            "checks": "10|20|10",
            "failure": 3
        },
        "exposure": {
            "limits": "100/3",
            "interactions": 10
        }
    },
    "resources": {
        "identifiers": {
            "internal": "n1fvkei1g11lcv",
            "social": "3616318175247400_pngjia"
        },
        "delayRange": "2000~3000"
    },
    "network": {
        "h5Config": {
            "hp": "com",
            "ttl": 10,
            "gateways": [
                "https://www.baidu.com=>2|5",
                "https://www.google.com"
            ]
        }
    }
}
    """

    // 增强版解析（带错误处理）
    fun safeParseGateways(gateways: String): Triple<String, Int, Int> {
            try {
                val parts = gateways.split("=>")
                require(parts.size == 2) { "Invalid format" }

                val limitParts = parts[1].split("|")
                require(limitParts.size == 2) { "Invalid limits" }

                val url = parts[0].trim()
                val hourlyLimit = limitParts[0].toInt()
                val dailyLimit = limitParts[1].toInt()

                return Triple(url, hourlyLimit, dailyLimit)
            } catch (e: Exception) {
                // 日志记录错误条目
                return Triple("", 0, 0)
            }

    }


}


