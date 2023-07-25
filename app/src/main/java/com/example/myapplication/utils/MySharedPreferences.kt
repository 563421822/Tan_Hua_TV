package com.example.myapplication.utils

import android.content.Context
import android.content.SharedPreferences

class MySharedPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    companion object {
        private const val SHARED_PREF_NAME = "my_shared_pref"
    }

    fun saveData(key: String, value: String) {
        editor.putString(key, value)
        editor.apply()
    }

    fun saveMultipleData(dataMap: Map<String, String>) {
        for ((key, value) in dataMap) {
            editor.putString(key, value)
        }
        editor.apply()
    }

    fun getData(key: String): String {
        return sharedPreferences.getString(key, "") ?: ""
    }
}