package dev.luizleal.tabuadaglecio.content

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class Theme(private val context: Context) {
    fun applyTheme() {
        val prefs: SharedPreferences = context.getSharedPreferences("TabuadaGlecioConfing",
            AppCompatActivity.MODE_PRIVATE
        )
        val theme = prefs.getString("theme", "system")

        when (theme) {
            "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}