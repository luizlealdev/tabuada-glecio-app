package dev.luizleal.tabuadaglecio.ui

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.luizleal.tabuadaglecio.R
import dev.luizleal.tabuadaglecio.content.SecurityPreferences
import dev.luizleal.tabuadaglecio.content.Theme
import dev.luizleal.tabuadaglecio.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private var settingsBinding: ActivitySettingsBinding? = null
    private val binding get() = settingsBinding!!

    private var isSpinnerInitialized = false

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        settingsBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = getSharedPreferences("TabuadaGlecioConfing", MODE_PRIVATE)


        binding.apply {
            textEditProfile.setOnClickListener(goToEditProfile())
            textDeleteProgress.setOnClickListener(deleteProgress())
            switchAnimations.setOnCheckedChangeListener(toggleAnimations())
            textVisitSite.setOnClickListener(goToWebsite())
        }

        Theme(applicationContext).applyTheme()

        setupThemeSpinner()
        setSwitchAnimationSelection()
    }

    private fun setupThemeSpinner() {
        ArrayAdapter.createFromResource(
            this,
            R.array.theme_mode,
            R.layout.spinner_item_layout
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_item_layout)
            binding.spinnerThemeSelector.adapter = adapter
        }

        // Configurar Spinner para selecionar o tema salvo
        setSpinnerSelection(binding.spinnerThemeSelector)

        binding.spinnerThemeSelector.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Verificar se o spinner foi inicializado para evitar loop infinito
                    if (isSpinnerInitialized) {
                        val selectedItem = parent?.getItemAtPosition(position).toString()

                        val themeToApply = when (selectedItem) {
                            "Sistema" -> "system"
                            "Claro" -> "light"
                            "Escuro" -> "dark"
                            else -> "system"
                        }


                        val currentTheme = prefs.getString("theme", "system")

                        if (themeToApply != currentTheme) {
                            saveTheme(themeToApply)
                            recreate()
                        }
                    } else {
                        isSpinnerInitialized = true
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Nenhuma ação necessária
                }
            }
    }

    private fun saveTheme(theme: String) {
        val editor: SharedPreferences.Editor =
            getSharedPreferences("TabuadaGlecioConfing", MODE_PRIVATE).edit()
        editor.putString("theme", theme)
        editor.apply()
    }

    private fun setSpinnerSelection(spinner: Spinner) {
        val theme = prefs.getString("theme", "system")

        when (theme) {
            "light" -> spinner.setSelection(1)
            "dark" -> spinner.setSelection(2)
            else -> spinner.setSelection(0)
        }
    }

    private fun toggleAnimations(): CompoundButton.OnCheckedChangeListener {
        return CompoundButton.OnCheckedChangeListener { _, isChecked ->
            val editor: SharedPreferences.Editor =
                getSharedPreferences("TabuadaGlecioConfing", MODE_PRIVATE).edit()

            editor.putString("animations", isChecked.toString())
            editor.apply()
        }
    }

    private fun setSwitchAnimationSelection() {
        val animations = prefs.getString("animations", "true").toBoolean()
        binding.switchAnimations.isChecked = animations
    }

    private fun deleteProgress(): View.OnClickListener {
        return View.OnClickListener { view ->
            MaterialAlertDialogBuilder(
                this@SettingsActivity,
                R.style.AlertDialogStyle
            ).setTitle("Tem certaza que desaja excluir seu progresso?")
                .setPositiveButton("" +
                        "Excluir") { _, _ ->

                    SecurityPreferences(applicationContext).storeString("maxScore", "0")

                }.setNegativeButton("Cancelar", null).show()
        }
    }

    private fun goToEditProfile(): View.OnClickListener {
        return View.OnClickListener { view ->
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            intent.putExtra("isEditing", true)
            startActivity(intent)
        }
    }

    private fun goToWebsite(): View.OnClickListener {
        return View.OnClickListener {
            val url = "https://tabuada-de-glecio.vercel.app/"
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        }
    }
}
