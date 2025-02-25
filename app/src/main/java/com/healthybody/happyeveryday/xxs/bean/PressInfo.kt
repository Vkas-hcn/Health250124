package com.healthybody.happyeveryday.xxs.bean

import com.healthybody.happyeveryday.xxs.util.Util

class PressInfo public constructor(val time:Long, val sys:Int, val dia:Int, val bpm:Int){

    val strTime :String
    init {
        strTime = Util.dateFormat(time)
    }

    fun text(): String {
        return "$time##$sys##$dia##$bpm"
    }
}