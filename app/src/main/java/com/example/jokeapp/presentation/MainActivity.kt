package com.example.jokeapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import com.example.jokeapp.JokeApp
import com.example.jokeapp.R
import com.example.jokeapp.views.CustomButton
import com.example.jokeapp.views.CustomImageButton
import com.example.jokeapp.views.CustomProgressBar
import com.example.jokeapp.views.CustomTextView

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = (application as JokeApp).mainViewModel
        val getJokeButton = findViewById<CustomButton>(R.id.actionButton)
        val favoriteButton = findViewById<CustomImageButton>(R.id.favoriteButton)
        val textView = findViewById<CustomTextView>(R.id.textView)
        val progressBar = findViewById<CustomProgressBar>(R.id.progressBar)
        progressBar.visibility = View.INVISIBLE

        val checkBox = findViewById<CheckBox>(R.id.checkbox)
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            mainViewModel.chooseFavorite(isChecked)
        }

        getJokeButton.setOnClickListener {
            mainViewModel.getJoke()
        }

        favoriteButton.setOnClickListener {
            mainViewModel.changeJokeStatus()
        }

        mainViewModel.observe(this) { state ->
            state.show(progressBar, getJokeButton, textView, favoriteButton)
        }
    }
}