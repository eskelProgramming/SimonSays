package com.example.simonsays

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class GameActivity : AppCompatActivity() {
    private lateinit var game: Game

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

        // Correctly find the TextView
        val tvDifficulty = findViewById<TextView>(R.id.tvDifficulty)

        // Set the difficulty text based on the game object
        when (game.difficulty) {
            EASY -> {
                tvDifficulty.text = "Difficulty: EASY"
            }
            MED -> {
                tvDifficulty.text = "Difficulty: MEDIUM"
            }
            HARD -> {
                tvDifficulty.text = "Difficulty: HARD"
            }
        }
    }
}