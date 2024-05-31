package dev.luizleal.tabuadaglecio.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.luizleal.tabuadaglecio.R
import dev.luizleal.tabuadaglecio.databinding.FragmentLeaderboardBinding

class LeaderboardFragment : Fragment() {
    private var leaderboardBinding: FragmentLeaderboardBinding? = null
    private val binding get() = leaderboardBinding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        leaderboardBinding = FragmentLeaderboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        leaderboardBinding = null
    }
}