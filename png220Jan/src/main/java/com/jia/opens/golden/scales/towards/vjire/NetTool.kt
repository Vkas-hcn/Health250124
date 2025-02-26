package com.jia.opens.golden.scales.towards.vjire


import android.annotation.SuppressLint
import android.util.Base64
import android.util.Log
import com.jia.opens.golden.scales.towards.pngstart.startApp
import com.jia.opens.golden.scales.towards.acan.ShowDataTool
import com.google.gson.Gson
import com.jia.opens.golden.scales.towards.jgri6sd.PngJiaBean
import org.json.JSONObject

import java.nio.charset.StandardCharsets

import java.io.BufferedInputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.util.concurrent.Executors

// 使用object声明单例（替代原双重校验锁模式）
object NetTool {
    interface ResultCallback {
        fun onComplete(result: String)
        fun onError(message: String)
    }

    private val threadPool = Executors.newFixedThreadPool(4)

    fun executeAdminRequest(callback: ResultCallback) {
        val requestData = prepareRequestData()
        ShowDataTool.showLog("executeAdminRequest=$requestData")
        TtPoint.postPointData(false, "reqadmin")
        threadPool.execute {
            var connection: HttpURLConnection? = null
            try {
                val (processedData, timestamp) = processRequestData(requestData)
                val targetUrl = URL(PngAllData.getConfig().adminUrl)
                Log.e(
                    "TAG",
                    "SanZong.ADMIN_URL=${startApp.mustXS}=: ${PngAllData.getConfig().adminUrl}",
                )
                connection = targetUrl.openConnection() as HttpURLConnection
                configureConnection(connection).apply {
                    setRequestProperty("timestamp", timestamp)
                    doOutput = true
                }

                connection.outputStream.use { os ->
                    os.write(processedData.toByteArray(StandardCharsets.UTF_8))
                }

                handleAdminResponse(connection, callback)
            }  catch (e: SocketTimeoutException) {
                callback.onError("Request timed out: ${e.message}")
                AppPointData.getadmin(12, "timeout")
            } catch (e: Exception) {
                callback.onError("Operation failed: ${e.message}")
                AppPointData.getadmin(13, "timeout")
            }finally {
                connection?.disconnect()
            }
        }
    }

    @SuppressLint("HardwareIds")
    private fun prepareRequestData(): String {
        return JSONObject().apply {
            put("nkPbI", "com.bpressure.sugarkeeper")
            put("TAktEnQ", startApp.okSpBean.appiddata)
            put("ygyC", startApp.okSpBean.refdata)
//            put("ygyC", "fb4a")
            put("DIOe", AppPointData.showAppVersion())
        }.toString()
    }

    private fun processRequestData(rawData: String): Pair<String, String> {
        val timestamp = System.currentTimeMillis().toString()
        val encrypted = xorEncrypt(rawData, timestamp)
        return Base64.encodeToString(encrypted.toByteArray(), Base64.NO_WRAP) to timestamp
    }

    private fun configureConnection(conn: HttpURLConnection): HttpURLConnection {
        conn.apply {
            connectTimeout = 60000
            readTimeout = 60000
            requestMethod = "POST"
            setRequestProperty("Content-Type", "application/json")
        }
        return conn
    }

    private fun handleAdminResponse(conn: HttpURLConnection, callback: ResultCallback) {
        try {
            val statusCode = conn.responseCode
            if (statusCode !in 200..299) {
                callback.onError("HTTP error: $statusCode")
                AppPointData.getadmin(11, statusCode.toString())
                return
            }

            BufferedInputStream(conn.inputStream).use { bis ->
                val responseString = InputStreamReader(bis).readText()
                val timestamp = conn.getHeaderField("timestamp")
                    ?: throw IllegalArgumentException("Missing timestamp header")

                // 解密处理
                val decodedBytes = Base64.decode(responseString, Base64.DEFAULT)
                val decodedStr = String(decodedBytes, StandardCharsets.UTF_8)
                val finalData = xorEncrypt(decodedStr, timestamp)

                // 解析数据
                val jsonResponse = JSONObject(finalData)
                val jsonData = parseAdminRefData(jsonResponse.toString())
                val adminBean = runCatching {
                    Gson().fromJson(jsonData, PngJiaBean::class.java)
                }.getOrNull()

                when {
                    adminBean == null -> {
                        callback.onError("Invalid response format")
                        AppPointData.getadmin(7, null)
                    }

                    ShowDataTool.getAdminData() == null -> {
                        ShowDataTool.putAdminData(jsonData)
                        val code = when {
                            adminBean?.appConfig?.userTier == 1 -> 1
                            else -> 2
                        }
                        AppPointData.getadmin(code, statusCode.toString())
                        callback.onComplete(jsonData)
                    }

                    adminBean.appConfig.userTier == 1 -> {
                        ShowDataTool.putAdminData(jsonData)
                        AppPointData.getadmin(1, statusCode.toString())
                        callback.onComplete(jsonData)
                    }

                    else -> {
                        AppPointData.getadmin(2, statusCode.toString())
                        callback.onComplete(jsonData)
                    }
                }
            }
        } catch (e: Exception) {
            callback.onError("Response processing failed: ${e.message}")
            AppPointData.getadmin(3, "parse_error")
        }
    }

    // 添加加解密方法（需与原有实现保持一致）
    private fun xorEncrypt(text: String, timestamp: String): String {
        val cycleKey = timestamp.toCharArray()
        val keyLength = cycleKey.size
        return text.mapIndexed { index, char ->
            char.toInt().xor(cycleKey[index % keyLength].toInt()).toChar()
        }.joinToString("")
    }

    private fun parseAdminRefData(jsonString: String): String {
        return try {
            JSONObject(jsonString).getJSONObject("asmIzvpK").getString("conf")
        } catch (e: Exception) {
            ""
        }
    }

    fun executePutRequest(body: Any, callback: ResultCallback) {
        threadPool.execute {
            var connection: HttpURLConnection? = null
            try {
                // 准备请求数据
                val jsonBodyString = JSONObject(body.toString()).toString()
                val targetUrl = URL(PngAllData.getConfig().upUrl)
                // 配置连接
                connection = targetUrl.openConnection() as HttpURLConnection
                configureConnection(connection).apply {
                    setRequestProperty("Content-Type", "application/json")
                    doOutput = true
                }

                // 发送请求
                connection.outputStream.use { os ->
                    os.write(jsonBodyString.toByteArray(StandardCharsets.UTF_8))
                }

                // 处理响应
                handlePutResponse(connection, callback)
            } catch (e: Exception) {
                callback.onError("Put request failed: ${e.message}")
            } finally {
                connection?.disconnect()
            }
        }
    }

    private fun handlePutResponse(conn: HttpURLConnection, callback: ResultCallback) {
        try {
            val statusCode = conn.responseCode
            if (statusCode !in 200..299) {
                callback.onError("HTTP error: $statusCode")
                return
            }

            BufferedInputStream(conn.inputStream).use { bis ->
                val responseString = InputStreamReader(bis).readText()
                callback.onComplete(responseString)
            }
        } catch (e: Exception) {
            callback.onError("Response processing failed: ${e.message}")
        }
    }

}



