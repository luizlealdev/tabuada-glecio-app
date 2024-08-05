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
import dev.luizleal.tabuadaglecio.ui.SettingsActivity
import dev.luizleal.tabuadaglecio.util.AnimationController.Companion.setButtonPressedAnimation

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding

    private lateinit var securityPreferences: SecurityPreferences

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

        securityPreferences = SecurityPreferences(requireContext())

        binding.buttonStart.setButtonPressedAnimation(requireContext())
        binding.buttonViewLeaderboard.setButtonPressedAnimation(requireContext())

        if (securityPreferences.getString("maxScore").isEmpty()) {
            securityPreferences.storeString("maxScore", "0")
        }

        binding.apply {
            buttonStart.setOnClickListener {
                it.findNavController().navigate(R.id.action_homeFragment_to_gameFragment)
            }
            buttonViewLeaderboard.setOnClickListener {
                val intent = Intent(requireContext(), LeaderboardActivity::class.java)
                startActivity(intent)
            }

            imageSettingsButton.setOnClickListener {
                val intent = Intent(requireContext(), SettingsActivity::class.java)
                startActivity(intent)
            }
        }

        setInfoToInterface()
    }

    override fun onResume() {
        super.onResume()
        setInfoToInterface()
    }

    private fun setInfoToInterface() {
        binding.textMaxScore.text =
            "Sua maior pontuação: ${SecurityPreferences(requireContext()).getString("maxScore")}"
    }
}