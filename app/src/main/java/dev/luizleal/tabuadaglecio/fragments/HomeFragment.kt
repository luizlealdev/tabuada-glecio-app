package dev.luizleal.tabuadaglecio.fragments

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import dev.luizleal.tabuadaglecio.R
import dev.luizleal.tabuadaglecio.databinding.FragmentHomeBinding
import dev.luizleal.tabuadaglecio.util.ViewUtils
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

        binding.buttonStart.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_gameFragment)
        }
        binding.buttonViewLeaderboard.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_leaderboardFragment)
        }
    }
}