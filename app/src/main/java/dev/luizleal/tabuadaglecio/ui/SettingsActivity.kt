package dev.luizleal.tabuadaglecio.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import dev.luizleal.tabuadaglecio.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private var settingsBinding: ActivitySettingsBinding? = null
    private val binding get() = settingsBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        settingsBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            textEditProfile.setOnClickListener(goToEditProfile())
        }
    }

    private fun goToEditProfile(): View.OnClickListener {
        return View.OnClickListener { view ->
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            intent.putExtra("isEditing", true)
            startActivity(intent)
        }
    }
}