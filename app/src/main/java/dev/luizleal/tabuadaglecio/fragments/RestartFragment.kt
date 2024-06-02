package dev.luizleal.tabuadaglecio.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import dev.luizleal.tabuadaglecio.R
import dev.luizleal.tabuadaglecio.content.SecurityPreferences
import dev.luizleal.tabuadaglecio.databinding.FragmentRestartBinding
import dev.luizleal.tabuadaglecio.ui.LeaderboardActivity
import dev.luizleal.tabuadaglecio.util.ViewUtils.Companion.setButtonPressedAnimation

class RestartFragment : Fragment(R.layout.fragment_restart) {
    private var restartBinding: FragmentRestartBinding? = null

    private lateinit var securityPreferences: SecurityPreferences
    
    private val binding get() = restartBinding!!
    private val args: RestartFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        restartBinding = FragmentRestartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setScore()

        binding.apply {
            buttonRestart.setButtonPressedAnimation()
            buttonViewLeaderboard.setButtonPressedAnimation()

            buttonRestart.setOnClickListener {
                it.findNavController().popBackStack(R.id.gameFragment, false)
            }
            buttonViewLeaderboard.setOnClickListener {
                val intent = Intent(requireContext(), LeaderboardActivity::class.java)
                startActivity(intent)
            }

            textMaxScore.text =
                "Sua maior pontuacão: ${SecurityPreferences(requireContext()).getString("maxScore")}"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        restartBinding = null
    }

    private fun setScore() {
        binding.textCountRight.setText(args.correctAnswers)
        binding.textCountWrong.setText(args.wrongAnswers)
    }
}