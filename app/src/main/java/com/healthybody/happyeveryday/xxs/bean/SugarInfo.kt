package com.healthybody.happyeveryday.xxs.bean

import com.healthybody.happyeveryday.xxs.util.Util

class SugarInfo public constructor(val time:Long, val unit:Int, val state:Int, val value:Double){

    val strTime :String
    init {
        strTime = Util.dateFormat(time)
    }

    fun text(): String {
        return "$time##$unit##$state##$value"
    }
}