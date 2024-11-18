package com.example.simonsays

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
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
    private lateinit var tvTimer: TextView
    private var timer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 0
    private var timerRunning: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Retrieve the Game object from the intent
        game = intent.getParcelableExtra("game") ?: throw IllegalStateException("Game object is missing")

        // Initialize the TextViews
        tvDifficulty = findViewById(R.id.tvDifficulty)
        tvTimer = findViewById(R.id.tvTimer)

        // Initialize the buttons
        btnHome = findViewById(R.id.btnReturnToHome)
        btnGreen = findViewById(R.id.btnGreen)
        btnRed = findViewById(R.id.btnRed)
        btnYellow = findViewById(R.id.btnYellow)
        btnBlue = findViewById(R.id.btnBlue)

        buttons = listOf(btnGreen, btnRed, btnYellow, btnBlue)

        // Set the difficulty text based on the game object
        when (game.difficulty) {
            EASY -> tvDifficulty.text = "Difficulty: EASY"
            MED -> tvDifficulty.text = "Difficulty: MEDIUM"
            HARD -> tvDifficulty.text = "Difficulty: HARD"
        }

        btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Start the game
        playGame()
    }

    private fun playGame() {
        pickButton() // Ensure at least one button is picked at the start
        showSequence {
            timeLeftInMillis = game.timer.toLong() * 1000
            startTimer()
            setButtonListeners()
        }
    }

    private fun showSequence(onSequenceShown: () -> Unit) {
        pauseTimer()
        val handler = Handler(Looper.getMainLooper())
        var delay = 0L

        for (buttonId in game.sequence) {
            val button = findViewById<Button>(buttonId.toInt())
            handler.postDelayed({
                lightUpButton(button)
            }, delay)

            delay += 1500 // 1.5 seconds delay between each button
        }

        handler.postDelayed({
            onSequenceShown()
        }, delay)
        restartTimer()
    }

    private fun pickButton() {
        val buttonIndex = Random.nextInt(0, buttons.size)
        val button = buttons[buttonIndex]

        game.sequence.add(button.id.toString())
        Log.d("GameActivity", "Button picked: ${button.id}")
        Log.d("GameActivity", "Current sequence: ${game.sequence}")
    }

    private fun lightUpButton(button: Button) {
        Log.d("GameActivity", "Lighting up button with ID: ${button.id}")

        // Set to light color
        button.setBackgroundColor(
            when (button.id) {
                R.id.btnGreen -> ContextCompat.getColor(this, R.color.lightGreen)
                R.id.btnRed -> ContextCompat.getColor(this, R.color.lightRed)
                R.id.btnYellow -> ContextCompat.getColor(this, R.color.lightYellow)
                R.id.btnBlue -> ContextCompat.getColor(this, R.color.lightBlue)
                else -> ContextCompat.getColor(this, R.color.skyBlue) // Fallback color
            }
        )

        Handler(Looper.getMainLooper()).postDelayed({
            Log.d("GameActivity", "Setting button back to dark color with ID: ${button.id}")

            // Set to dark color
            button.setBackgroundColor(
                when (button.id) {
                    R.id.btnGreen -> ContextCompat.getColor(this, R.color.darkGreen)
                    R.id.btnRed -> ContextCompat.getColor(this, R.color.darkRed)
                    R.id.btnYellow -> ContextCompat.getColor(this, R.color.darkYellow)
                    R.id.btnBlue -> ContextCompat.getColor(this, R.color.darkBlue)
                    else -> ContextCompat.getColor(this, R.color.skyBlue) // Fallback color
                }
            )
        }, 1000) // 1000 milliseconds delay
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerText()
            }

            override fun onFinish() {
                tvTimer.text = "Time's up!"
                game.gameOver = true
                // Handle end of game logic here
            }
        }.start()

        timerRunning = true
    }

    private fun pauseTimer() {
        timer?.cancel()
        timerRunning = false
    }

    private fun updateTimerText() {
        val secondsLeft = (timeLeftInMillis / 1000).toInt()
        tvTimer.text = "Timer: $secondsLeft"
    }

    private fun setButtonListeners() {
        for (button in buttons) {
            button.setOnClickListener {
                // Handle button press
                restartTimer()

                // Logic to check if the button pressed is correct
                if (game.sequence[game.currentIndex] == button.id.toString()) {
                    game.currentIndex++
                } else {
                    game.gameOver = true
                    tvTimer.text = "Wrong Button!"
                }

                if (game.currentIndex >= game.sequence.size) {
                    // Move to next round
                    game.currentIndex = 0
                    pickButton()
                    showSequence {
                        restartTimer()
                    }
                }
            }
        }
    }

    private fun restartTimer() {
        pauseTimer()
        timeLeftInMillis = game.timer.toLong() * 1000
        startTimer()
    }
}