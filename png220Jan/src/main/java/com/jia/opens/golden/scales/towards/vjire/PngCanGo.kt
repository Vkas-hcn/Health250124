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
    fun startService(activity: Activity) {
        if (KEY_IS_SERVICE) {
            ShowDataTool.showLog("FebFiveFffService-startService---4-----$KEY_IS_SERVICE")
            return
        }
        ShowDataTool.showLog("FebFiveFffService-startService---1-----$KEY_IS_SERVICE")
        // 检查 Android 14+ 的前台服务特殊用途权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasSpecialUsePermission = ContextCompat.checkSelfPermission(
                mainStart,
                Manifest.permission.FOREGROUND_SERVICE_SPECIAL_USE
            ) == PackageManager.PERMISSION_GRANTED
            ShowDataTool.showLog("hasSpecialUsePermission: $hasSpecialUsePermission")
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
}