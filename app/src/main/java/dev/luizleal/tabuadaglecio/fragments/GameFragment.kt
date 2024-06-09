package dev.luizleal.tabuadaglecio.fragments

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dev.luizleal.tabuadaglecio.R
import dev.luizleal.tabuadaglecio.content.SecurityPreferences
import dev.luizleal.tabuadaglecio.databinding.FragmentGameBinding
import dev.luizleal.tabuadaglecio.model.LeaderboardUser
import dev.luizleal.tabuadaglecio.model.Multiplication
import dev.luizleal.tabuadaglecio.util.ViewUtils.Companion.setButtonPressedAnimation
import dev.luizleal.tabuadaglecio.util.ViewUtils.Companion.setButtonPressedAnimationToAll
import dev.luizleal.tabuadaglecio.util.ViewUtils.Companion.setScaleAnimation
import kotlin.random.Random

class GameFragment : Fragment(R.layout.fragment_game) {
    private var gameBinding: FragmentGameBinding? = null
    private val binding get() = gameBinding!!

    private lateinit var resultInput: EditText
    private lateinit var currentMultiplication: Multiplication
    private var lastMultiplication = Multiplication(1, 1, 1)
    private var correctCount = 0
    private var wrongCount = 0

    private var firebaseDatabase: FirebaseDatabase? = null
    private var databaseReference: DatabaseReference? = null
    private var databaseGlobalLeaderboardReference: DatabaseReference? = null

    private lateinit var securityPreferences: SecurityPreferences
    private lateinit var prefsConfig: SharedPreferences
    private var animationState: Boolean = true


    //private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        gameBinding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resultInput = binding.editResultInput

        val numpadButtons = listOf(
            binding.textNumpad0,
            binding.textNumpad1,
            binding.textNumpad2,
            binding.textNumpad3,
            binding.textNumpad4,
            binding.textNumpad5,
            binding.textNumpad6,
            binding.textNumpad7,
            binding.textNumpad8,
            binding.textNumpad9
        )

        binding.apply {
            textNumpadEnter.setButtonPressedAnimation()
            textNumpadBackspace.setButtonPressedAnimation()
        }

        securityPreferences = SecurityPreferences(requireContext())

        prefsConfig = requireContext().getSharedPreferences("TabuadaGlecioConfing", MODE_PRIVATE)
        animationState = prefsConfig.getString("animations", "true").toBoolean()

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase?.getReference("leaderboard")
        databaseGlobalLeaderboardReference = firebaseDatabase?.getReference("leaderboard_global")

        setButtonPressedAnimationToAll(numpadButtons)
        setNumpadButtonsAction(numpadButtons)

        startTimer()

        generateNewMultiplication()
        setMultiplication()

        handleBackspaceButton()

        binding.textNumpadEnter.setOnClickListener {
            submitAnswer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        gameBinding = null
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun handleBackspaceButton() {
        binding.textNumpadBackspace.setOnClickListener {
            resultInput.setText("${resultInput.text.dropLast(1)}")
        }
    }

    private fun submitAnswer() {
        val userAnswer = resultInput.text.toString()

        if (userAnswer.isNotEmpty()) {
            try {
                val userAnswerInt = userAnswer.toInt()
                val result = currentMultiplication.result

                if (userAnswerInt == result) {
                    correctCount++
                } else {
                    wrongCount++
                }

                updateInterface()
            } catch (e: NumberFormatException) {
                Toast.makeText(context, "Por favor, insira um número válido", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(context, "Por favor, insira um número", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setNumpadButtonsAction(buttons: List<TextView>) {
        buttons.forEach { button ->
            button.setOnClickListener {
                resultInput.setText("${resultInput.text}${button.text}")
            }
        }
    }

    private fun startTimer() {
        val progressBar = binding.progressbarTime
        val duration = 61_000L
        progressBar.max = 1000

        val countDownTimer = object : CountDownTimer(duration, 50) {
            override fun onTick(millisUntilFinished: Long) {
                val progress = (millisUntilFinished.toFloat() / duration * 1000).toInt()
                progressBar.progress = progress
                //resultInput.setText(progress)
            }

            override fun onFinish() {
                progressBar.progress = 0

                val action = GameFragmentDirections.actionGameFragmentToRestartFragment(
                    correctCount.toString(),
                    wrongCount.toString()
                )

                databaseReference?.let {
                    saveLeaderboardItem(
                        it, LeaderboardUser(
                            username = securityPreferences.getString("username"),
                            userClass = securityPreferences.getString("userClass"),
                            avatarId = securityPreferences.getString("avatarId"),
                            score = correctCount
                        )
                    )
                }
                checkMaxScore()

                correctCount = 0
                wrongCount = 0
                resultInput.setText(null)

                if (isAdded && !isRemoving) {
                    findNavController().navigate(action)
                } else {
                    Log.e("GameFragment", "Fragment not attached to the context or being removed")
                }
            }
        }

        countDownTimer.start()  // Iniciar a contagem regressiva
    }

    private fun updateInterface() {
        binding.textCorrectCount.setText("Acertos: ${correctCount}")
        binding.textWrongCount.setText("Erros: ${wrongCount}")

        generateNewMultiplication()
        setMultiplication()

        resultInput.setText(null)
    }

    private fun setMultiplication() {
        val textMultiplication = binding.textMultiplication

        lastMultiplication = currentMultiplication
        textMultiplication.setText(
            "${currentMultiplication.firstNumber} x ${currentMultiplication.secondNumber}"
        )

        if (animationState) textMultiplication.setScaleAnimation()
    }

    private fun generateNewMultiplication() {
        val firstNumber = Random.nextInt(2, 10)
        val secondNumber = Random.nextInt(1, 10)
        val result = firstNumber * secondNumber

        if (lastMultiplication.firstNumber != firstNumber && lastMultiplication.secondNumber != secondNumber) {
            currentMultiplication = Multiplication(firstNumber, secondNumber, result)
        } else {
            generateNewMultiplication()
        }
    }

    private fun saveLeaderboardItem(reference: DatabaseReference, data: LeaderboardUser) {
        reference.child(securityPreferences.getString("userId")).setValue(data)
    }

    private fun checkMaxScore() {
        val maxScore = if (securityPreferences.getString("maxScore")
                .isNotEmpty()
        ) securityPreferences.getString("maxScore").toInt() else 0

        if (correctCount >= maxScore) {
            securityPreferences.storeString("maxScore", correctCount.toString())
            databaseGlobalLeaderboardReference?.let {
                saveLeaderboardItem(
                    it, LeaderboardUser(
                        username = securityPreferences.getString("username"),
                        userClass = securityPreferences.getString("userClass"),
                        avatarId = securityPreferences.getString("avatarId"),
                        score = securityPreferences.getString("maxScore").toInt()
                    )
                )
            }
        }
    }
}