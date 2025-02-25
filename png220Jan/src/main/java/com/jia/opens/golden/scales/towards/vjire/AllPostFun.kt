package com.jia.opens.golden.scales.towards.vjire

import android.content.Context
import com.anythink.core.api.ATAdInfo
import com.jia.opens.golden.scales.towards.vjire.SanPutData.upAdJson
import com.jia.opens.golden.scales.towards.vjire.SanPutData.upInstallJson
import com.jia.opens.golden.scales.towards.vjire.SanPutData.upPointJson
import com.jia.opens.golden.scales.towards.pngstart.MainStart
import com.jia.opens.golden.scales.towards.pngstart.MainStart.canIntNextFun
import com.jia.opens.golden.scales.towards.pngstart.MainStart.mainStart
import com.jia.opens.golden.scales.towards.acan.ShowDataTool
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resumeWithException
import kotlin.random.Random
import kotlin.coroutines.resume

object AllPostFun {
    // 在类顶部添加协程作用域
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    sealed class Result<out T> {
        data class Success<out T>(val value: T) : Result<T>()
        data class Failure(val exception: Throwable) : Result<Nothing>()
    }

    fun onePostAdmin(isFistPost: Boolean) {
        scope.launch {
            val bean = ShowDataTool.getAdminData()
            val delay = Random.nextLong(1000, 20 * 60 * 1000) // 1秒到20分钟
            if (!isFistPost && bean != null && bean.appConfig.userTier==1) {
                canIntNextFun()
                ShowDataTool.showLog("冷启动app延迟 ${delay}ms 请求admin数据")
                delay(delay)
            }
            executeWithRetry(
                maxRetries = 3,
                minDelay = 10_000L,
                maxDelay = 40_000L
            ) { attempt ->
                try {
                    val result = NetworkOperator.executeAdminRequestSuspend()
                    val bean = ShowDataTool.getAdminData()
                    ShowDataTool.showLog("admin-data: $result")
                    if (bean != null && bean.appConfig.userTier!=1) {
                        ShowDataTool.showLog("不是A用户，进行重试")
                        bPostAdmin()
                    }
                    if (bean?.appConfig?.userTier==1 && isFistPost) {
                        canIntNextFun()
                    }
                    Result.Success(Unit)
                } catch (e: Exception) {
                    handleError(3, "Admin", e, attempt)
                    Result.Failure(e)
                }
            }
        }
    }

    private fun bPostAdmin() {
        scope.launch {
            // 执行带重试机制的请求
            executeWithRetry(
                maxRetries = 20,
                minDelay = 59_000L,
                maxDelay = 60_000L
            ) { attempt ->
                try {
                    val result = NetworkOperator.executeAdminRequestSuspend()
                    val bean = ShowDataTool.getAdminData()
                    ShowDataTool.showLog("admin-onSuccess: $result")
                    if (bean != null && bean.appConfig.userTier!=1) {
                        handleError(20, "不是A用户，进行重试", Exception(), attempt)
                        Result.Failure(Exception())
                    } else {
                        canIntNextFun()
                        Result.Success(Unit)
                    }
                } catch (e: Exception) {
                    handleError(20, "Admin", e, attempt)
                    Result.Failure(e)
                }
            }
        }
    }

    fun postInstallData(context: Context) {
        scope.launch {
            val data = withContext(Dispatchers.Default) {
                MainStart.saveBean.IS_INT_JSON.ifEmpty {
                    val newData = upInstallJson(context)
                    MainStart.saveBean.IS_INT_JSON = newData
                    newData
                }
            }

            ShowDataTool.showLog("Install: data=$data")

            // 执行带重试机制的请求
            executeWithRetry(
                maxRetries = 20,
                minDelay = 10_000L,
                maxDelay = 40_000L
            ) { attempt ->
                try {
                    val result = NetworkOperator.executePutRequestSuspend(data)
                    handleSuccess("Install", result)
                    MainStart.saveBean.IS_INT_JSON = ""
                    Result.Success(Unit)
                } catch (e: Exception) {
                    handleError(20, "Install", e, attempt)
                    Result.Failure(e)
                }
            }
        }
    }

    fun postAdData(adValue: ATAdInfo) {
        scope.launch {
            // 准备请求数据
            val data = upAdJson(mainStart, adValue)
            ShowDataTool.showLog("postAd: data=$data")
            // 执行带重试机制的请求
            executeWithRetry(
                maxRetries = 3,
                minDelay = 10_000L,
                maxDelay = 40_000L
            ) { attempt ->
                try {
                    val result = NetworkOperator.executePutRequestSuspend(data)
                    handleSuccess("Ad", result)
                    Result.Success(Unit)
                } catch (e: Exception) {
                    handleError(3, "Ad", e, attempt)
                    Result.Failure(e)
                }
            }
            SanPutData.postAdValue(adValue)
        }
    }

    fun postPointData(
        isAdMinCon: Boolean,
        name: String,
        key1: String? = null,
        keyValue1: Any? = null,
        key2: String? = null,
        keyValue2: Any? = null
    ) {
        scope.launch {
            val adminBean = ShowDataTool.getAdminData()
            if (!isAdMinCon && adminBean?.appConfig?.dataSync == false) {
                return@launch
            }
            // 准备请求数据
            val data = if (key1 != null) {
                upPointJson(name, key1, keyValue1, key2, keyValue2)
            } else {
                upPointJson(name)
            }
            ShowDataTool.showLog("Point-${name}-开始打点--${data}")
            // 执行带重试机制的请求
            val maxNum = if (isAdMinCon) {
                20
            } else {
                3
            }
            executeWithRetry(
                maxRetries = maxNum,
                minDelay = 10_000L,
                maxDelay = 40_000L
            ) { attempt ->
                try {
                    val result = NetworkOperator.executePutRequestSuspend(data)
                    handleSuccess("Point-${name}", result)
                    Result.Success(Unit)
                } catch (e: Exception) {
                    handleError(maxNum, "Point-${name}", e, attempt)
                    Result.Failure(e)
                }
            }
        }
    }

    // 带随机延迟的重试执行器
    private suspend fun <T> executeWithRetry(
        maxRetries: Int,
        minDelay: Long,
        maxDelay: Long,
        block: suspend (attempt: Int) -> Result<T>
    ) {
        repeat(maxRetries) { attempt ->
            when (val result = block(attempt)) {
                is Result.Success -> return
                is Result.Failure -> {
                    val delayTime = Random.nextLong(minDelay, maxDelay)
                    delay(delayTime)
                }
            }
        }
    }

    private suspend fun NetworkOperator.executeAdminRequestSuspend(): String {
        return suspendCancellableCoroutine { continuation ->
            executeAdminRequest(object : NetworkOperator.ResultCallback {
                override fun onComplete(result: String) {
                    continuation.resume(result)
                }

                override fun onError(message: String) {
                    continuation.resumeWithException(Exception(message))
                }
            })
        }
    }

    // 挂起函数扩展
    private suspend fun NetworkOperator.executePutRequestSuspend(data: String): String {
        return suspendCancellableCoroutine { continuation ->
            executePutRequest(data, object : NetworkOperator.ResultCallback {
                override fun onComplete(result: String) {
                    continuation.resume(result)
                }

                override fun onError(message: String) {
                    continuation.resumeWithException(Exception(message))
                }
            })
        }
    }

    // 处理成功响应
    private fun handleSuccess(type: String, result: String) {
        ShowDataTool.showLog("${type}-请求成功: $result")
    }

    // 处理错误日志
    private fun handleError(maxNum: Int, type: String, e: Exception, attempt: Int) {
        ShowDataTool.showLog(
            """
        ${type}-请求失败[重试次数:${attempt + 1}]: ${e.message}
        ${if (attempt >= maxNum - 1) "达到最大重试次数" else ""}
    """.trimIndent()
        )
    }

}