package dev.luizleal.tabuadaglecio.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dev.luizleal.tabuadaglecio.R
import dev.luizleal.tabuadaglecio.adapter.LeaderboardUserAdapter
import dev.luizleal.tabuadaglecio.databinding.FragmentScoreboardBinding
import dev.luizleal.tabuadaglecio.model.LeaderboardUser

class ScoreboardFragment : Fragment(R.layout.fragment_scoreboard) {
    private var scoreboardBinding: FragmentScoreboardBinding? = null
    private val binding get() = scoreboardBinding!!

    private var firebaseDatabase: FirebaseDatabase? = null
    private var databaseReference: DatabaseReference? = null
    private var leaderboardUserList = mutableListOf<LeaderboardUser>()

    private var adapter: LeaderboardUserAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        scoreboardBinding = FragmentScoreboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase?.getReference("leaderboard")

        getData()

        initRecyclerView()
    }


    private fun initRecyclerView() {
        adapter = LeaderboardUserAdapter()
        binding.apply {
            recyclerLeaderboard.layoutManager = LinearLayoutManager(requireContext())
            recyclerLeaderboard.adapter = adapter
        }
    }

    private fun getData() {
        databaseReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                leaderboardUserList.clear()

                for (data in snapshot.children) {
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
                    requireContext(),
                    "Ocorreu um erro, verifique sua conexão com a internet e tente novamente.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}