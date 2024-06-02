package dev.luizleal.tabuadaglecio.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import dev.luizleal.tabuadaglecio.R
import dev.luizleal.tabuadaglecio.content.SecurityPreferences
import dev.luizleal.tabuadaglecio.databinding.FragmentHomeBinding
import dev.luizleal.tabuadaglecio.ui.LeaderboardActivity
import dev.luizleal.tabuadaglecio.util.ViewUtils.Companion.setButtonPressedAnimation

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonStart.setButtonPressedAnimation()
        binding.buttonViewLeaderboard.setButtonPressedAnimation()

        binding.apply {
            buttonStart.setOnClickListener {
                it.findNavController().navigate(R.id.action_homeFragment_to_gameFragment)
            }
            buttonViewLeaderboard.setOnClickListener {
                val intent = Intent(requireContext(), LeaderboardActivity::class.java)
                startActivity(intent)
            }
            textMaxScore.text =
                "Sua maior pontuação: ${SecurityPreferences(requireContext()).getString("maxScore")}"
        }
    }
}