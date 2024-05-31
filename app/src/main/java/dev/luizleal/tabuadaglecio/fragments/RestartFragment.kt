package dev.luizleal.tabuadaglecio.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavArgs
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import dev.luizleal.tabuadaglecio.R
import dev.luizleal.tabuadaglecio.databinding.FragmentRestartBinding
import dev.luizleal.tabuadaglecio.util.ViewUtils.Companion.setButtonPressedAnimation

class RestartFragment : Fragment(R.layout.fragment_restart) {
    private var restartBinding: FragmentRestartBinding? = null
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

        val startButton = binding.buttonStart
        val viewLeaderboardButton = binding.buttonStart

        startButton.setButtonPressedAnimation()
        viewLeaderboardButton.setButtonPressedAnimation()

        setScore()

        startButton.setOnClickListener {
            it.findNavController().popBackStack(R.id.gameFragment, false)
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