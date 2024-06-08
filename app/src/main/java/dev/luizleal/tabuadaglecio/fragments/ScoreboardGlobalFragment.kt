package dev.luizleal.tabuadaglecio.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dev.luizleal.tabuadaglecio.adapter.LeaderboardUserAdapter
import dev.luizleal.tabuadaglecio.databinding.FragmentScoreboardGlobalBinding
import dev.luizleal.tabuadaglecio.model.LeaderboardUser

class ScoreboardGlobalFragment : Fragment() {
    private var globalScoreboardBinding: FragmentScoreboardGlobalBinding? = null
    private val binding get() = globalScoreboardBinding!!

    private var firebaseDatabase: FirebaseDatabase? = null
    private var databaseReference: DatabaseReference? = null
    private var leaderboardUserList = mutableListOf<LeaderboardUser>()

    private var adapter: LeaderboardUserAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        globalScoreboardBinding =
            FragmentScoreboardGlobalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase?.getReference("leaderboard_global")

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
        databaseReference?.orderByChild("score")?.limitToFirst(60)
            ?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    leaderboardUserList.clear()

                    val top60Keys = mutableSetOf<String>()

                    for (data in snapshot.children) {
                        val username = data.child("username").value.toString()
                        val userClass = data.child("userClass").value.toString()
                        val avatarId = data.child("avatarId").value.toString()
                        val score = data.child("score").value.toString().toInt()

                        val userId = data.key ?: continue
                        top60Keys.add(userId)

                        leaderboardUserList.add(
                            LeaderboardUser(
                                username = username,
                                userClass = userClass,
                                avatarId = avatarId,
                                score = score
                            )
                        )
                    }

                    Log.d("snapshot", leaderboardUserList.toString())

                    val sortedLeaderboardList: List<LeaderboardUser> =
                        leaderboardUserList.sortedByDescending { it.score }
                    adapter?.setItems(sortedLeaderboardList.toMutableList())

                    binding.loadingSpinner.visibility = View.GONE

                    // Remover itens adicionais que não estão nos top 60
                    databaseReference?.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(allItemsSnapshot: DataSnapshot) {
                            for (item in allItemsSnapshot.children) {
                                val key = item.key
                                if (key != null && !top60Keys.contains(key)) {
                                    databaseReference!!.child(key).removeValue()
                                        .addOnSuccessListener {
                                            Log.d("Delete Item", "Deleted item with key: $key")
                                        }
                                        .addOnFailureListener { exception ->
                                            Log.e(
                                                "Delete Item Error",
                                                "Failed to delete item with key: $key, error: ${exception.message}"
                                            )
                                        }
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.e("Get Data Error", "onCancelled: ${error.toException()}")
                        }
                    })
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Get Data Error", "onCancelled: ${error.toException()}")

                    Toast.makeText(
                        requireContext(),
                        "Ocorreu um erro, verifique sua conexão com a internet e tente novamente.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

}