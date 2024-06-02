package dev.luizleal.tabuadaglecio.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dev.luizleal.tabuadaglecio.R
import dev.luizleal.tabuadaglecio.adapter.LeaderboardUserAdapter
import dev.luizleal.tabuadaglecio.databinding.ActivityLeaderboardBinding
import dev.luizleal.tabuadaglecio.model.LeaderboardUser

class LeaderboardActivity : AppCompatActivity() {

    private var leaderboardBinding: ActivityLeaderboardBinding? = null
    private val binding get() = leaderboardBinding!!

    private var firebaseDatabase: FirebaseDatabase? = null
    private var databaseReference: DatabaseReference? = null
    private var leaderboardUserList = mutableListOf<LeaderboardUser>()

    private var adapter: LeaderboardUserAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        leaderboardBinding = ActivityLeaderboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase?.getReference("leaderboard")

        getData()

        initRecyclerView()
    }


    private fun initRecyclerView() {
        adapter = LeaderboardUserAdapter()
        binding.apply {
            recyclerLeaderboard.layoutManager = LinearLayoutManager(applicationContext)
            recyclerLeaderboard.adapter = adapter
        }
    }

    private fun getData() {
        databaseReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                leaderboardUserList.clear()

                for (data in snapshot.children) {
                    val id = data.key
                    val username = data.child("username").value.toString()
                    val userClass = data.child("userClass").value.toString()
                    val avatarId = data.child("avatarId").value.toString()
                    val score = data.child("score").value.toString()

                    leaderboardUserList.add(
                        LeaderboardUser(
                            username = username,
                            userClass = userClass,
                            avatarId = avatarId,
                            score = score.toInt()
                        )
                    )
                }
                Log.d("snapshot", leaderboardUserList.toString())

                val sortedLeaderboardList: List<LeaderboardUser> = leaderboardUserList.sortedByDescending { it.score }
                adapter?.setItems(sortedLeaderboardList.toMutableList())

                binding.loadingSpinner.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Get Data Error", "onCanceled: ${error.toException()}")

                Toast.makeText(
                    applicationContext,
                    "Ocorreu um erro, verifique sua conexão com a internet e tente novamente.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}