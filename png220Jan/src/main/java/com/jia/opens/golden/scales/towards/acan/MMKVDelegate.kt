package com.jia.opens.golden.scales.towards.acan

import com.tencent.mmkv.MMKV
import kotlin.reflect.KProperty

class MMKVDelegate<T>(private val key: String, private val defaultValue: T) {
    private val mmkv = MMKV.defaultMMKV()

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return when (defaultValue) {
            is Boolean -> mmkv.decodeBool(key, defaultValue)
            is Int -> mmkv.decodeInt(key, defaultValue)
            is Long -> mmkv.decodeLong(key, defaultValue)
            is Float -> mmkv.decodeFloat(key, defaultValue)
            is Double -> mmkv.decodeDouble(key, defaultValue)
            is String -> mmkv.decodeString(key, defaultValue)
            else -> throw IllegalArgumentException("Unsupported type")
        } as T
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        when (value) {
            is Boolean -> mmkv.encode(key, value)
            is Int -> mmkv.encode(key, value)
            is Long -> mmkv.encode(key, value)
            is Float -> mmkv.encode(key, value)
            is Double -> mmkv.encode(key, value)
            is String -> mmkv.encode(key, value)
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }
}
