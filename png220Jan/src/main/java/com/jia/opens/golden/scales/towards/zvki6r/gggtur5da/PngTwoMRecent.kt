package com.jia.opens.golden.scales.towards.zvki6r.gggtur5da

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import androidx.annotation.Keep

@Keep
class PngTwoMRecent: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.hasExtra("M")) {
            val eIntent = intent.getParcelableExtra<Parcelable>("M") as Intent?
            if (eIntent != null) {
                try {
                    context.startActivity(eIntent)
                    return
                } catch (e: Exception) {
                }
            }
        }
    }
}