package com.healthybody.happyeveryday.xxs

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainCanGoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val facebookPackageName = "com.android.chrome"
        try {
            this.packageManager.getPackageInfo(facebookPackageName, 0)
            val intent = this.packageManager.getLaunchIntentForPackage(facebookPackageName)
            if (intent != null) {
                this.startActivity(intent)
                finish()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            val uri = Uri.parse("market://details?id=$facebookPackageName")
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            try {
                this.startActivity(goToMarket)
                finish()
            } catch (exception: ActivityNotFoundException) {
                this.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$facebookPackageName")
                    )
                )
                finish()
            }
        }
    }

}