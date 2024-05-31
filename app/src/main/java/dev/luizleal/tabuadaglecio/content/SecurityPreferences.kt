package dev.luizleal.tabuadaglecio.content

import android.content.Context
import android.content.SharedPreferences

class SecurityPreferences(context: Context) {
    private val securityPreferences: SharedPreferences =
        context.getSharedPreferences("TabuadaDeGlecio", Context.MODE_PRIVATE)

    fun storeString(key: String, value: String) {
        securityPreferences.edit().putString(key, value).apply()
    }

    fun getString(key: String): String {
        return securityPreferences.getString(key, "") ?: ""
    }
}