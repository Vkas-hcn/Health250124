package com.jia.opens.golden.scales.towards.vjire


import android.content.Context
import android.content.SharedPreferences
import kotlin.reflect.KProperty

class SharedPreferencesDelegate<T>(
    private val context: Context,
    private val key: String,
    private val defaultValue: T
) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return when (defaultValue) {
            is Boolean -> sharedPreferences.getBoolean(key, defaultValue)
            is Int -> sharedPreferences.getInt(key, defaultValue)
            is Long -> sharedPreferences.getLong(key, defaultValue)
            is Float -> sharedPreferences.getFloat(key, defaultValue)
            is String -> sharedPreferences.getString(key, defaultValue) ?: defaultValue
            else -> throw IllegalArgumentException("Unsupported type")
        } as T
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        with(sharedPreferences.edit()) {
            when (value) {
                is Boolean -> putBoolean(key, value)
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is Float -> putFloat(key, value)
                is String -> putString(key, value)
                else -> throw IllegalArgumentException("Unsupported type")
            }.apply()
        }
    }
}
