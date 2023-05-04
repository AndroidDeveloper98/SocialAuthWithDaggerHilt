package com.innovation.socialauthwithdaggerhiltmvvm.utils

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSharedPreference @Inject constructor(@ApplicationContext context: Context) {
    private val appSharedPreference: SharedPreferences
    private val keyAppSharedPreference = "APP_SHARED_PREFERENCE"
    val keyUserName = "USER_NAME"
    val keyEmailAddress = "EMAIL_ADDRESS"
    val keyProfileImage = "PROFILE_IMAGE"
    val keyLoginType = "LOGIN_TYPE"
    private val keyUserLogin = "USER_LOGIN"

    init {
        appSharedPreference = context.getSharedPreferences(keyAppSharedPreference, Context.MODE_PRIVATE)
    }

    fun getString(key : String): String {
        return appSharedPreference.getString(key, "") ?: ""
    }

    fun setString(key : String, data: String) {
        appSharedPreference.edit().putString(key, data).apply()
    }

    fun isUserLogin(): Boolean {
        return appSharedPreference.getBoolean(keyUserLogin,false)
    }

    fun setIsUserLogin(data: Boolean) {
        appSharedPreference.edit().putBoolean(keyUserLogin, data).apply()
    }

    fun clearSharedPreference() {
        appSharedPreference.edit().clear().apply()
    }

}