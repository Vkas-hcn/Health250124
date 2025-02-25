package com.jia.opens.golden.scales.towards.pngstart

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.jia.opens.golden.scales.towards.vjire.NetworkOperator
import com.jia.opens.golden.scales.towards.acan.ShowDataTool

class OneHW(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        ShowDataTool.showLog("Admin request started")
        NetworkOperator.executeAdminRequest(object : NetworkOperator.ResultCallback {
            override fun onComplete(result: String) {
                ShowDataTool.showLog("Admin request successful: $result")
            }

            override fun onError(message: String) {
                ShowDataTool.showLog("Admin request failed: $message")
            }
        })
        return Result.success()
    }
}
