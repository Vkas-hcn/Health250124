package com.healthybody.happyeveryday.xxs.util

import android.content.Intent
import android.util.Log
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Util {

    fun dateFormat(ms: Long): String {
        val str = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(ms))
        return str
    }

    fun toast(msg:String){
        Toast.makeText(App.app,msg,Toast.LENGTH_SHORT).show();
    }

    inline fun i(call:() -> String){
        Log.i("HHHH",call.invoke())
    }


    fun getWebIntent(url:String): Intent {
       return Intent.parseUri(url,Intent.URI_INTENT_SCHEME)
    }
}