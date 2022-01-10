package com.tuwaiq.halfway.utility


interface IPreferencesHelper {

    fun getAll(): MutableMap<String, *>

    fun getString(key: String): String

    fun putString(key: String, value: String)

    fun getInt(key: String): Int

    fun putInt(key: String, value: Int)

    fun getBoolean(key: String): Boolean

    fun putBoolean(key: String, value: Boolean)

    fun getLong(key: String): Long

    fun putLong(key: String, value: Long)

    fun clearPreferences()

}