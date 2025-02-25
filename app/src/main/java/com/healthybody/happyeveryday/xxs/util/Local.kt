package com.healthybody.happyeveryday.xxs.util

import com.healthybody.happyeveryday.xxs.bean.PressInfo
import com.healthybody.happyeveryday.xxs.bean.SugarInfo
import com.tencent.mmkv.MMKV

object Local {


    lateinit var mmkv: MMKV

    fun putSet(key:String, value:Set<String>){
        mmkv.encode(key,value)
    }
    fun getSet(key:String): MutableSet<String> {
        return mmkv.decodeStringSet(key)?: mutableSetOf<String>()
    }


    val KEY_PRESS_INFO = "key_press_Info"

    fun addPressInfo( info: PressInfo){
        val set = getSet(KEY_PRESS_INFO)
        set.add(info.text())
        putSet(KEY_PRESS_INFO,set)
    }

    fun getAllPressInfo(): MutableList<PressInfo> {
        val set = getSet(KEY_PRESS_INFO)
        val list = mutableListOf<PressInfo>()
        for ((index, ss) in set.withIndex()) {
            val arr = ss.split("##")
            val info = PressInfo(arr.get(0).toLong(),arr.get(1).toInt(),arr.get(2).toInt(),arr.get(3).toInt())
            list.add(info)
        }
        list.sortByDescending { it.time }
        return list
    }
    fun rmPressInfo(info: PressInfo) {
        val set = getSet(KEY_PRESS_INFO)
        for ((index, ss) in set.withIndex()) {
            if (ss.startsWith("${info.time}")){
                set.remove(ss)
                putSet(KEY_PRESS_INFO,set)
                break
            }
        }
    }

    fun updatePressInfo(info: PressInfo) {
        val set = getSet(KEY_PRESS_INFO)
        for ((index, ss) in set.withIndex()) {
            if (ss.startsWith("${info.time}")){
                set.remove(ss)
                set.add(info.text())
                putSet(KEY_PRESS_INFO,set)
                break
            }
        }
    }



    //////////////
    val KEY_SUGAR_INFO = "KEY_SUGAR_INFO"

    fun addSugarInfo( info: SugarInfo){
        val set = getSet(KEY_SUGAR_INFO)
        set.add(info.text())
        putSet(KEY_SUGAR_INFO,set)
    }

    fun getAllSugarInfo(): MutableList<SugarInfo> {
        val set = getSet(KEY_SUGAR_INFO)
        val list = mutableListOf<SugarInfo>()
        for ((index, ss) in set.withIndex()) {
            val arr = ss.split("##")
            val info = SugarInfo(arr.get(0).toLong(),arr.get(1).toInt(),arr.get(2).toInt(),arr.get(3).toDouble())
            list.add(info)
        }
        list.sortByDescending { it.time }
        return list
    }

    fun rmSugarInfo(info: SugarInfo) {
        val set = getSet(KEY_SUGAR_INFO)
        for ((index, ss) in set.withIndex()) {
            if (ss.startsWith("${info.time}")){
                set.remove(ss)
                putSet(KEY_SUGAR_INFO,set)
                break
            }
        }
    }

    fun updateSugarInfo(info: SugarInfo) {
        val set = getSet(KEY_SUGAR_INFO)
        for ((index, ss) in set.withIndex()) {
            if (ss.startsWith("${info.time}")){
                set.remove(ss)
                set.add(info.text())
                putSet(KEY_SUGAR_INFO,set)
                break
            }
        }
    }
}