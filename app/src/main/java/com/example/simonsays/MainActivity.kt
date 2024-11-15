package com.example.simonsays

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private var difficulty: Double = EASY
    private lateinit var difficultyButton: Button

    // buttons
    private lateinit var btnEasy: Button
    private lateinit var btnMed: Button
    private lateinit var btnHard: Button
    private lateinit var btnStartGame: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize the buttons
        btnEasy = findViewById(R.id.btnEasy)
        btnMed = findViewById(R.id.btnMed)
        btnHard = findViewById(R.id.btnHard)
        btnStartGame = findViewById(R.id.btnStartGame)

        // Initialize the difficultyButton after btnEasy is initialized
        difficultyButton = btnEasy

        val difficultyButtons = listOf(btnEasy, btnMed, btnHard)

        // set difficulty button onClickListener
        for (button in difficultyButtons) {
            button.setOnClickListener { view -> onDifficultyButtonClick(view) }
        }

        // set start button onClickListener
        btnStartGame.setOnClickListener {
            val game = Game(difficulty)

            val intent = Intent(this, GameActivity::class.java).apply {
                putExtra("game", game)
            }
            startActivity(intent)
        }
    }

    private fun onDifficultyButtonClick(view: View) {
        val button = view as Button
        when (button.text) {
            "Easy" -> {
                if (difficultyButton.text == "Medium") {
                    difficultyButton.setBackgroundColor(ContextCompat.getColor(this, R.color.darkYellow))
                } else {
                    difficultyButton.setBackgroundColor(ContextCompat.getColor(this, R.color.darkRed))
                }
                button.setBackgroundColor(ContextCompat.getColor(this, R.color.lightGreen))
                difficulty = EASY
            }
            "Medium" -> {
                if (difficultyButton.text == "Easy") {
                    difficultyButton.setBackgroundColor(ContextCompat.getColor(this, R.color.darkGreen))
                } else {
                    difficultyButton.setBackgroundColor(ContextCompat.getColor(this, R.color.darkRed))
                }
                button.setBackgroundColor(ContextCompat.getColor(this, R.color.lightYellow))
                difficulty = MED
            }
            "Hard" -> {
                if (difficultyButton.text == "Easy") {
                    difficultyButton.setBackgroundColor(ContextCompat.getColor(this, R.color.darkGreen))
                } else {
                    difficultyButton.setBackgroundColor(ContextCompat.getColor(this, R.color.darkYellow))
                }
                button.setBackgroundColor(ContextCompat.getColor(this, R.color.lightRed))
                difficulty = HARD
            }
        }
        difficultyButton = button
    }
}