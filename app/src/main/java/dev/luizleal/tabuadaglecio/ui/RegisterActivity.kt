package dev.luizleal.tabuadaglecio.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.FirebaseDatabase
import dev.luizleal.tabuadaglecio.R
import dev.luizleal.tabuadaglecio.adapter.AvatarSelectorAdapter
import dev.luizleal.tabuadaglecio.content.SecurityPreferences
import dev.luizleal.tabuadaglecio.databinding.ActivityRegisterBinding
import dev.luizleal.tabuadaglecio.model.AvatarItem
import dev.luizleal.tabuadaglecio.model.LeaderboardUser
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
        binding.buttonSave.setButtonPressedAnimation()

        initRecyclerView()

        val avatarList: MutableList<AvatarItem> = mutableListOf()
        for (i in 1..32) {
            avatarList += AvatarItem("avatar-$i")
        }
        adapter?.setItems(avatarList)

        securityPreferences.storeString("avatarId", "avatar-1")
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
        val intent = intent
        val isEditingProfile = intent.getBooleanExtra("isEditing", false)

        if (securityPreferences.getString("username").isNotEmpty()) {
            if (!isEditingProfile) {
                goToMainActivity()
            } else {
                setUserInfo()
            }
        }
    }

    private fun saveUserInfo(): View.OnClickListener {
        return View.OnClickListener { view ->
            val intent = intent
            val isEditingProfile = intent.getBooleanExtra("isEditing", false)

            val username = binding.editName.text
            val userClass = binding.editClass.text

            if (username.isNotEmpty() && userClass.isNotEmpty()) {
                securityPreferences.storeString("username", username.toString().trim())
                securityPreferences.storeString("userClass", userClass.toString().trim())

                val userId =
                    if (isEditingProfile) securityPreferences.getString("userId") else StringUtils.generateUserId()
                securityPreferences.storeString("userId", userId)

                //Log.d("RegisterActivity", "Navigating to MainActivity")

                if (!isEditingProfile) {
                    goToMainActivity()
                } else {
                    editUserInfoInGlobalDatabase()
                    finish()
                }

            } else {
                MaterialAlertDialogBuilder(
                    this@RegisterActivity,
                    R.style.AlertDialogStyle
                ).setTitle("Algo deu errado!").setMessage("Por favor, preencha todos os campos")
                    .setPositiveButton("Ok", null).show()
            }
        }
    }

    private fun setUserInfo() {
        binding.apply {
            editName.setText(securityPreferences.getString("username"))
            editClass.setText(securityPreferences.getString("userClass"))
        }
    }

    private fun editUserInfoInGlobalDatabase() {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.getReference("leaderboard_global")

        databaseReference.child(securityPreferences.getString("userId")).setValue(
            LeaderboardUser(
                username = securityPreferences.getString("username"),
                userClass = securityPreferences.getString("userClass"),
                avatarId = securityPreferences.getString("avatarId"),
                score = securityPreferences.getString("maxScore").toInt()
            )
        )
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}