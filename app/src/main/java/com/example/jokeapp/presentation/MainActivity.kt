package com.example.jokeapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jokeapp.JokeApp
import com.example.jokeapp.R

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel = (application as JokeApp).mainViewModel

        val favoriteDataView = findViewById<FavoriteDataView>(R.id.jokeFavoriteDataView).apply {

            listenChanges { isChecked ->
                mainViewModel.chooseFavorite(isChecked)
            }

            handleFavoriteButton {
                mainViewModel.changeJokeStatus()
            }

            handleActionButton {
                mainViewModel.getJoke()
            }
        }

        mainViewModel.observe(this) { state ->
            favoriteDataView.show(state)
        }
    }
}