package com.jia.opens.golden.scales.towards.vjire


import android.annotation.SuppressLint
import android.util.Base64
import android.util.Log
import com.jia.opens.golden.scales.towards.jgri6sd.SanZong
import com.jia.opens.golden.scales.towards.pngstart.MainStart
import com.jia.opens.golden.scales.towards.acan.ShowDataTool
import com.google.gson.Gson
import com.jia.opens.golden.scales.towards.jgri6sd.PngJiaBean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject

import java.nio.charset.StandardCharsets

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Url
import java.io.BufferedInputStream
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

// 使用object声明单例（替代原双重校验锁模式）
object NetworkOperator {
    interface ResultCallback {
        fun onComplete(result: String)
        fun onError(message: String)
    }
    private val threadPool = Executors.newFixedThreadPool(4)

    fun executeAdminRequest2(callback: ResultCallback) {
        val requestData = prepareRequestData()
        ShowDataTool.showLog("executeAdminRequest=$requestData")

        threadPool.execute {
            var connection: HttpURLConnection? = null
            try {
                val (processedData, timestamp) = processRequestData(requestData)
                val targetUrl = URL(SanZong.getConfig().adminUrl)
                Log.e("TAG", "SanZong.ADMIN_URL=${MainStart.mustXS}=: ${SanZong.getConfig().adminUrl}", )
                connection = targetUrl.openConnection() as HttpURLConnection
                configureConnection(connection).apply {
                    setRequestProperty("timestamp", timestamp)
                    doOutput = true
                }

                connection.outputStream.use { os ->
                    os.write(processedData.toByteArray(StandardCharsets.UTF_8))
                }

                handleAdminResponse(connection, callback)
            } catch (e: Exception) {
                callback.onError("Operation failed: ${e.message}")
                SanPutData.getadmin(12, "timeout")
            } finally {
                connection?.disconnect()
            }
        }
    }

    @SuppressLint("HardwareIds")
    private fun prepareRequestData(): String {
        return JSONObject().apply {
            put("uCmc", "com.wallframe.artstyle.magic")
            put("OIpnL", MainStart.saveBean.appiddata)
            put("dtntFH", MainStart.saveBean.refdata)
//            put("dtntFH", "fb4a")
            put("noebBGeYSM", SanPutData.showAppVersion())
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
                SanPutData.getadmin(11, statusCode.toString())
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
                        SanPutData.getadmin(7, null)
                    }

                    ShowDataTool.getAdminData() == null -> {
                        ShowDataTool.putAdminData(jsonData)
                        val code = when {
                            adminBean?.appConfig?.userTier == 1 -> 1
                            else -> 2
                        }
                        SanPutData.getadmin(code, statusCode.toString())
                        callback.onComplete(jsonData)
                    }

                    adminBean.appConfig.userTier == 1 -> {
                        ShowDataTool.putAdminData(jsonData)
                        SanPutData.getadmin(1, statusCode.toString())
                        callback.onComplete(jsonData)
                    }

                    else -> {
                        SanPutData.getadmin(2, statusCode.toString())
                        callback.onComplete(jsonData)
                    }
                }
            }
        } catch (e: Exception) {
            callback.onError("Response processing failed: ${e.message}")
            SanPutData.getadmin(3, "parse_error")
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
            JSONObject(jsonString).getJSONObject("ehY").getString("conf")
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
                val targetUrl = URL(SanZong.getConfig().upUrl)
                Log.e("TAG", "executePutRequest=${MainStart.mustXS}=: ${SanZong.getConfig().upUrl}", )
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










        // Retrofit相关配置
        private val apiService by lazy {
            Retrofit.Builder()
                .baseUrl("https://your.base.url/") // 实际URL通过方法动态获取
                .client(
                    OkHttpClient.Builder()
                        .connectTimeout(60, TimeUnit.MILLISECONDS)
                        .readTimeout(60, TimeUnit.MILLISECONDS)
                        .build()
                )
                .addConverterFactory(ScalarsConverterFactory.create()) // 使用字符串转换器
                .build()
                .create(ApiService::class.java)
        }

        // Retrofit接口定义（新增）
        private interface ApiService {
            @POST
            @Headers("Content-Type: application/json")
            suspend fun postAdminRequest(
                @Url dynamicUrl: String,
                @Header("timestamp") timestamp: String,
                @Body body: RequestBody
            ): Response<String>

            @POST
            @Headers("Content-Type: application/json")
            suspend fun postPutRequest(
                @Url dynamicUrl: String,
                @Body body: RequestBody
            ): Response<String>
        }

        // 改造后的请求方法（使用协程替代线程池）
        fun executeAdminRequest(callback: ResultCallback) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val requestData = prepareRequestData() // 保持原有方法不变
                    val (processedData, timestamp) = processRequestData(requestData)
                    ShowDataTool.showLog("executeAdminRequest=$requestData")

                    // Retrofit网络调用
                    val response = apiService.postAdminRequest(
                        dynamicUrl = SanZong.getConfig().adminUrl,
                        timestamp = timestamp,
                        body = processedData.toRequestBody("application/json".toMediaType())
                    )

                    handleNetworkResponse(response, timestamp, callback)
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        callback.onError("Operation failed: ${e.message}")
                        SanPutData.getadmin(11, "timeout")
                    }
                }
            }
        }

        // 统一响应处理方法（新增）
        private fun handleNetworkResponse(
            response: Response<String>,
            timestamp: String,
            callback: ResultCallback
        ) {
            if (!response.isSuccessful) {
                callback.onError("HTTP error: ${response.code()}")
                SanPutData.getadmin(12, response.code().toString())
                return
            }

            response.body()?.let { encryptedResponse ->
                try {
                    // 解密处理（保持原有逻辑不变）
                    val decodedBytes = Base64.decode(encryptedResponse, Base64.DEFAULT)
                    val decodedStr = String(decodedBytes, StandardCharsets.UTF_8)
                    val finalData = xorEncrypt(decodedStr, timestamp)

                    // 后续业务逻辑处理（保持原有代码不变）
                    val jsonResponse = JSONObject(finalData)
                    /* 原有数据处理逻辑... */

                    // 主线程回调
                    CoroutineScope(Dispatchers.Main).launch {
                        callback.onComplete(finalData)
                    }
                } catch (e: Exception) {
                    /* 异常处理... */
                }
            }
        }


}



