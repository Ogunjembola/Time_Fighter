package com.example.timefighter

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName
    private lateinit var your_score: TextView
    private lateinit var time_left: TextView
    private lateinit var tap_me_btn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "OnCreate called.Score is :$score")

        your_score = findViewById(R.id.your_score)
        time_left = findViewById(R.id.time_left)
        tap_me_btn = findViewById(R.id.tap_me_btn)
        resetGame()

        tap_me_btn.setOnClickListener { v ->
            val bounceAnimation = AnimationUtils.loadAnimation(
                this,
                R.anim.bounce
            )
            v.startAnimation(bounceAnimation)
            incrementScore()
            if (!gameStarted) {
                startGame()
            }

        }
    }

    companion object {
        private const val SCORE_KEY = "SCORE_KEY"
        private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putInt(SCORE_KEY, score)
        outState.putInt(TIME_LEFT_KEY, timeLeft)
        countDownTimer.cancel()
        Log.d(TAG, "onSaveInstanceState: Saving Score: $score & Time Left:$timeLeft")

    }
    // 3
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called.")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.about_item) {
            showInfo()
        }
        return true
    }

    private fun showInfo() {
        val dialogTitle = getString(
            R.string.about_title,
            BuildConfig.VERSION_NAME
        )
        val dialogMessage = getString(R.string.about_message)
        val builder = AlertDialog.Builder(this)
        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMessage)
        builder.create().show()
    }

    private var score = 0

    private fun incrementScore() {
        // increment score logic
        score++

        val newScore = " Your Score:$score"
        your_score.text = newScore

    }

    private var gameStarted = false
    private lateinit var countDownTimer: CountDownTimer
    private var initialCountdown: Long = 60000
    private var countDownInterval: Long = 1000
    private var timeLeft = 60
    private fun resetGame() {
        // reset game logic
        // setting the initial score to zero
        score = 0
        val initialScore = score
        your_score.text = initialScore.toString()

        // setting the initial time to zero
        countDownTimer = object : CountDownTimer(initialCountdown, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished.toInt() / 1000

                val timeLeftString = getString(R.string.time_left_1_d, timeLeft)
                time_left.text = timeLeftString
            }

            override fun onFinish() {
                endGame()
            }

        }
        gameStarted = false


    }

    private fun startGame() {
        // start game logic

        countDownTimer.start()
        gameStarted = true
    }

    @SuppressLint("StringFormatInvalid")
    private fun endGame() {
        // end game logic
        Toast.makeText(
            this, getString(R.string.game_ver_message, score),
            Toast.LENGTH_LONG
        ).show()
        resetGame()
    }
}