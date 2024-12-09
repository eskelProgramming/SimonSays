package com.example.simonsays

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

class GameActivity : AppCompatActivity() {
    private lateinit var game: Game
    private lateinit var btnHome: Button
    private lateinit var btnGreen: Button
    private lateinit var btnRed: Button
    private lateinit var btnYellow: Button
    private lateinit var btnBlue: Button
    private lateinit var buttons: List<Button>
    private lateinit var tvDifficulty: TextView
    private lateinit var tvScore: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        game = intent.getParcelableExtra("game") ?: throw IllegalStateException("Game object is missing")

        tvDifficulty = findViewById(R.id.tvDifficulty)
        tvScore = findViewById(R.id.tvScore)

        btnHome = findViewById(R.id.btnReturnToHome)
        btnGreen = findViewById(R.id.btnGreen)
        btnRed = findViewById(R.id.btnRed)
        btnYellow = findViewById(R.id.btnYellow)
        btnBlue = findViewById(R.id.btnBlue)

        buttons = listOf(btnGreen, btnRed, btnYellow, btnBlue)

        when (game.difficulty) {
            EASY -> tvDifficulty.text = "Difficulty: EASY"
            MED -> tvDifficulty.text = "Difficulty: MEDIUM"
            HARD -> tvDifficulty.text = "Difficulty: HARD"
        }

        btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        setButtonListeners()

        Handler(Looper.getMainLooper()).postDelayed({
            playGame()
        }, 1000)
    }

    private fun playGame() {
        if (game.gameOver) {
            return
        }
        selectButton()
        displaySequence()
    }

    private fun selectButton() {
        val button = buttons[Random.nextInt(0, 4)]
        game.sequence.add(button)
    }

    private fun displaySequence() {
        disableButtons()
        val handler = Handler(Looper.getMainLooper())

        var delay = 0L

        for (button in game.sequence) {
            handler.postDelayed({
                lightUpButton(button)
            }, delay)
            delay += (1500 / game.difficulty).toLong() // Move the delay forward for the next button
        }

        // Enable the buttons after the entire sequence has been displayed and turned off
        enableButtons()
    }

    private fun lightUpButton(button: Button) {
        val handler = Handler(Looper.getMainLooper())

        button.setBackgroundColor(
            when (button.id) {
                R.id.btnGreen -> ContextCompat.getColor(this, R.color.lightGreen)
                R.id.btnRed -> ContextCompat.getColor(this, R.color.lightRed)
                R.id.btnYellow -> ContextCompat.getColor(this, R.color.lightYellow)
                R.id.btnBlue -> ContextCompat.getColor(this, R.color.lightBlue)
                else -> ContextCompat.getColor(this, R.color.skyBlue)
            }
        )

        handler.postDelayed({
            button.setBackgroundColor(
                when (button.id) {
                    R.id.btnGreen -> ContextCompat.getColor(this, R.color.darkGreen)
                    R.id.btnRed -> ContextCompat.getColor(this, R.color.darkRed)
                    R.id.btnYellow -> ContextCompat.getColor(this, R.color.darkYellow)
                    R.id.btnBlue -> ContextCompat.getColor(this, R.color.darkBlue)
                    else -> ContextCompat.getColor(this, R.color.skyBlue)
                }
            )
        }, (1000 / game.difficulty).toLong()) // Turn off the button after the time is up

    }


    private fun setButtonListeners() {
        for (button in buttons) {
            button.setOnClickListener {
                // Logic to check if the button pressed is correct
                if (game.sequence[game.currentIndex].id == button.id) {
                    game.currentIndex++

                } else {
                    game.gameOver = true
                    disableButtons()
                    tvScore.text = "Game over! Final Score: ${game.sequence.size - 1}"
                }
                if (game.currentIndex >= game.sequence.size) {
                    // Move to next round
                    game.currentIndex = 0
                    tvScore.text = "Score: ${game.sequence.size}"
                    playGame()
                }
            }
        }
    }

    private fun disableButtons() {
        buttons.forEach { it.isEnabled = false }
    }

    private fun enableButtons() {
        buttons.forEach { it.isEnabled = true}
    }
}