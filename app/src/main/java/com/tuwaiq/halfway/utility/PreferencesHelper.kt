package com.tuwaiq.halfway.utility

import android.content.Context
import android.content.SharedPreferences
import com.tuwaiq.halfway.utility.Constant.SharedPref.Companion.SHARED_PREF


class PreferencesHelper
internal constructor(context: Context) : IPreferencesHelper {

    private val mPrefs: SharedPreferences =
        context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)

    override fun getAll(): MutableMap<String, *> = mPrefs.all

    override fun getString(key: String) = mPrefs.getString(key, null) ?: ""

    override fun putString(key: String, value: String) = mPrefs.edit().putString(key, value).apply()

    override fun getInt(key: String) = mPrefs.getInt(key, 0)

    override fun putInt(key: String, value: Int) = mPrefs.edit().putInt(key, value).apply()

    override fun getBoolean(key: String) = mPrefs.getBoolean(key, false)

    override fun putBoolean(key: String, value: Boolean) =
        mPrefs.edit().putBoolean(key, value).apply()

    override fun getLong(key: String) = mPrefs.getLong(key, 0)

    override fun putLong(key: String, value: Long) = mPrefs.edit().putLong(key, value).apply()

    override fun clearPreferences() = mPrefs.edit().clear().apply()
}
