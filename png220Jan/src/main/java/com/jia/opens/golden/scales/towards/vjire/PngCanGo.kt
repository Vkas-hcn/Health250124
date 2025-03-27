package com.jia.opens.golden.scales.towards.vjire

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Process
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import com.jia.opens.golden.scales.towards.zvki6r.fie3h.PngTwoFService
import com.jia.opens.golden.scales.towards.pngstart.startApp.mainStart
import com.jia.opens.golden.scales.towards.acan.ShowDataTool
import java.util.ArrayList

object PngCanGo {
    var KEY_IS_SERVICE = false
    var activityList = ArrayList<Activity>()
    var isH5State = false
    const val REQUEST_CODE_FOREGROUND_SERVICE_PERMISSIONS = 1101
    fun closeAllActivities() {
        ShowDataTool.showLog("closeAllActivities")
        for (activity in activityList) {
            activity.finishAndRemoveTask()
        }
        activityList.clear()
    }

    fun addActivity(activity: Activity) {
        activityList.add(activity)
    }

    fun removeActivity(activity: Activity) {
        activityList.remove(activity)
    }

    fun getInstallTimeDataFun(): Long {
        try {
            val packageManager: PackageManager = mainStart.packageManager
            val packageInfo: PackageInfo = packageManager.getPackageInfo(mainStart.packageName, 0)
            val firstInstallTime: Long = packageInfo.firstInstallTime
            return (System.currentTimeMillis() - firstInstallTime) / 1000
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return 0L
        }
    }

    fun getInstallFast(): Long {
        try {
            val packageManager: PackageManager = mainStart.packageManager
            val packageInfo: PackageInfo = packageManager.getPackageInfo(mainStart.packageName, 0)
            return packageInfo.firstInstallTime
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return 0L
        }
    }

    private val handlerService = Handler(Looper.getMainLooper())
    private var runnableService: Runnable? = null
    fun startService(activity: Activity) {
        if (KEY_IS_SERVICE) {
            ShowDataTool.showLog("FebFiveFffService-startService---4-----$KEY_IS_SERVICE")
            return
        }
        ShowDataTool.showLog("FebFiveFffService-startService---1-----$KEY_IS_SERVICE")
        // 检查 Android 14+ 的前台服务特殊用途权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionStatus = ContextCompat.checkSelfPermission(mainStart, Manifest.permission.FOREGROUND_SERVICE_SPECIAL_USE)
            ShowDataTool.showLog("hasSpecialUsePermission: $permissionStatus")
                requestPermissions(
                    activity,
                    arrayOf(
                        Manifest.permission.POST_NOTIFICATIONS,
                        Manifest.permission.FOREGROUND_SERVICE_SPECIAL_USE
                    ), REQUEST_CODE_FOREGROUND_SERVICE_PERMISSIONS
                )
                ShowDataTool.showLog("FebFiveFffService-startService---2-----Requesting special use permission")
                return
        } else {
            ShowDataTool.showLog("FebFiveFffService-startService---3-----$KEY_IS_SERVICE")
            ContextCompat.startForegroundService(
                mainStart,
                Intent(mainStart, PngTwoFService::class.java)
            )
        }

    }

    fun startService2() {
        stopService()
        runnableService = object : Runnable {
            override fun run() {
                ShowDataTool.showLog("FebFiveFffService-startService---1-----$KEY_IS_SERVICE")
                if (!KEY_IS_SERVICE && Build.VERSION.SDK_INT < 31) {
                    ShowDataTool.showLog("FebFiveFffService-startService---2-----$KEY_IS_SERVICE")
                    ContextCompat.startForegroundService(
                        mainStart,
                        Intent(mainStart, PngTwoFService::class.java)
                    )
                } else {
                    ShowDataTool.showLog("FebFiveFffService-startService---3-----$KEY_IS_SERVICE")
                    stopService()
                    return
                }

                handlerService.postDelayed(this, 1020)
            }
        }
        handlerService.postDelayed(runnableService!!, 1020)
    }

    private fun stopService() {
        runnableService?.let {
            handlerService.removeCallbacks(it)
            runnableService = null
        }
    }

    fun isMainProcess(context: Context): Boolean {
        val currentProcessName = getCurrentProcessName(context)
        return currentProcessName == context.packageName
    }

    private fun getCurrentProcessName(context: Context): String? {
        val pid = Process.myPid()
        val activityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
        for (processInfo in activityManager.runningAppProcesses) {
            if (processInfo.pid == pid) {
                return processInfo.processName
            }
        }
        return null
    }

    fun getSupportedAbi(): String {
        // 优先检测64位架构
        for (abi in Build.SUPPORTED_64_BIT_ABIS) {
            if (abi.startsWith("arm64") || abi.startsWith("x86_64")) {
                return abi
            }
        }
        for (abi in Build.SUPPORTED_32_BIT_ABIS) {
            if (abi.startsWith("armeabi") || abi.startsWith("x86")) {
                return abi
            }
        }
        return Build.CPU_ABI
    }

    fun getAssetName(abi: String, isH5: Boolean): String {
        // 根据架构选择加密文件名
        if (abi.contains("64")) {
            val assetName = if (isH5) {
                "h8.txt"
            } else {
                "pngJia8.txt"
            }
            return assetName // 64位加密文件
        }
        val assetName = if (isH5) {
            "h7.txt"
        } else {
            "pngJia7.txt"
        }
        return assetName // 32位加密文件
    }
}