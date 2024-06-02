package dev.luizleal.tabuadaglecio.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.luizleal.tabuadaglecio.R
import dev.luizleal.tabuadaglecio.adapter.AvatarSelectorAdapter
import dev.luizleal.tabuadaglecio.content.SecurityPreferences
import dev.luizleal.tabuadaglecio.databinding.ActivityRegisterBinding
import dev.luizleal.tabuadaglecio.model.AvatarItem
import dev.luizleal.tabuadaglecio.util.StringUtils
import dev.luizleal.tabuadaglecio.util.ViewUtils.Companion.setButtonPressedAnimation

class RegisterActivity : AppCompatActivity() {
    private var registerBinding: ActivityRegisterBinding? = null
    private val binding get() = registerBinding!!

    private lateinit var securityPreferences: SecurityPreferences

    private var adapter: AvatarSelectorAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        securityPreferences = SecurityPreferences(applicationContext)
        checkIfUserAlreadyRegisted()

        binding.buttonSave.setOnClickListener(saveUserInfo())
        binding.buttonSave.setButtonPressedAnimation();

        initRecyclerView()

        val avatarList: MutableList<AvatarItem> = mutableListOf()
        for (i in 1..32) {
            avatarList += AvatarItem("avatar-$i")
        }
        adapter?.setItems(avatarList)
    }

    private fun initRecyclerView() {
        adapter = AvatarSelectorAdapter(applicationContext, binding.avatarLoadingSpinner)
        binding.apply {
            recyclerAvatarSelector.layoutManager =
                LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
            recyclerAvatarSelector.adapter = adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        registerBinding = null
    }

    private fun checkIfUserAlreadyRegisted() {
        if (securityPreferences.getString("username").isNotEmpty()) {
            goToMainActivity()
        }
    }

    private fun saveUserInfo(): View.OnClickListener {
        return View.OnClickListener { view ->
            val username = binding.editName.text
            val userClass = binding.editClass.text

            if (username.isNotEmpty() && userClass.isNotEmpty()) {
                securityPreferences.storeString("username", username.toString())
                securityPreferences.storeString("userClass", userClass.toString())
                securityPreferences.storeString("userId", StringUtils.generateUserId())

                //Log.d("RegisterActivity", "Navigating to MainActivity")

                goToMainActivity()
            } else {
                MaterialAlertDialogBuilder(
                    this@RegisterActivity,
                    R.style.AlertDialogStyle
                ).setTitle("Algo deu errado!").setMessage("Por favor, preencha todos os campos")
                    .setPositiveButton("Ok", null).show()
            }
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}