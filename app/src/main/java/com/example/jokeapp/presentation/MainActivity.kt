package com.example.jokeapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import com.example.jokeapp.JokeApp
import com.example.jokeapp.R

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = (application as JokeApp).mainViewModel
        val getJokeButton = findViewById<Button>(R.id.actionButton)
        val favoriteButton = findViewById<ImageButton>(R.id.favoriteButton)
        val textView = findViewById<TextView>(R.id.textView)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.INVISIBLE

        val checkBox = findViewById<CheckBox>(R.id.checkbox)
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            mainViewModel.chooseFavorite(isChecked)
        }
        getJokeButton.setOnClickListener {
            getJokeButton.isEnabled = false
            progressBar.visibility = View.VISIBLE
            mainViewModel.getJoke()
        }
        favoriteButton.setOnClickListener {
            mainViewModel.changeJokeStatus()
        }

        val jokeUiCallback = object : JokeUiCallback {
            override fun provideText(text: String) {
                progressBar.visibility = View.GONE
                getJokeButton.isEnabled = true
                textView.text = text
            }

            override fun provideResId(resId: Int) {
                favoriteButton.setImageResource(resId)
            }
        }
        mainViewModel.observe(this) {
            it.show(jokeUiCallback)
        }

    }
}